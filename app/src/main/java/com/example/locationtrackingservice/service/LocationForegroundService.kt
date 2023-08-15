package com.example.locationtrackingservice.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build.*
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.locationtrackingservice.MainActivity
import com.example.locationtrackingservice.R
import com.example.locationtrackingservice.database.LocationEntity
import com.example.locationtrackingservice.repository.LocationRepository
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachine
import com.example.locationtrackingservice.stateMachine.States
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class LocationForegroundService : Service() {
    private val stateMachine: LocationTrackingStateMachine by inject()
    private val locationRepository: LocationRepository by inject()
    private val scope = CoroutineScope(Dispatchers.Main)

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "LocationForegroundService"
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, LocationForegroundService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, LocationForegroundService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setupNotification(intent)
        observeStateMachine()

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun setupNotification(intent: Intent?) {
        createNotificationChannel()
        val input = intent?.getStringExtra("inputExtra") ?: ""
        val notification = buildNotification(input)
        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    private fun buildNotification(input: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Location Tracking Service is running")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun observeStateMachine() {
        stateMachine.currentState.observeForever { state ->
            if (state == States.RUNNING) {
                observeLocationUpdatesAndSaveToDatabase()
            } else if (state == States.DONE) {
                cancel()
            }
        }
    }

    private fun observeLocationUpdatesAndSaveToDatabase() {
        stateMachine.getLocationUpdates()
            .onEach {
                if (it != null) {
                    saveLocationToDatabase(it)
                }
            }
            .launchIn(scope)
    }

    private suspend fun saveLocationToDatabase(location: Location) {
        val locationEntity = LocationEntity(location = "$location")
        locationRepository.insertLocation(locationEntity)
    }

    private fun cancel() = scope.coroutineContext.cancelChildren()
}