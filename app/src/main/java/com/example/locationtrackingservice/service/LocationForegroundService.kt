package com.example.locationtrackingservice.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build.*
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.locationtrackingservice.KEY_LOCATION_DATA
import com.example.locationtrackingservice.LOG_TAG_STATE
import com.example.locationtrackingservice.MainActivity
import com.example.locationtrackingservice.R
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachine
import com.example.locationtrackingservice.stateMachine.States
import com.example.locationtrackingservice.worker.SaveLocationWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class LocationForegroundService : Service() {
    private val stateMachine: LocationTrackingStateMachine by inject()
    private val scope = CoroutineScope(Dispatchers.Main)


    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "1"
        private const val NOTIFICATION_CHANNEL_NAME = "Service Channel"
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
                NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    private fun buildNotification(input: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(packageName)
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun observeStateMachine() {
        stateMachine.currentState.observeForever { state ->
            when (state) {
                States.RUNNING -> observeLocationUpdatesAndSaveToDatabase()
                States.DONE -> cancel()
                else -> {
                    Log.d(LOG_TAG_STATE, "ERROR")
                    cancel()
                    cancelPeriodicWorkRequest()
                    stopService(applicationContext)
                }
            }
        }
    }

    private fun observeLocationUpdatesAndSaveToDatabase() {
        stateMachine.getLocationUpdates()
            .onEach { location ->
                if (location != null) {
                    saveLocationToDatabase(location)
                }
            }
            .launchIn(scope)
    }

    private fun saveLocationToDatabase(location: Location) {
        val data = workDataOf(KEY_LOCATION_DATA to location.toString())
        val saveLocationRequest = PeriodicWorkRequestBuilder<SaveLocationWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setInputData(data)
            .build()
        WorkManager.getInstance(application).enqueue(saveLocationRequest)
    }

    private fun cancel() = scope.coroutineContext.cancelChildren()

    private fun cancelPeriodicWorkRequest() {
        WorkManager.getInstance(applicationContext)
            .cancelAllWorkByTag(SaveLocationWorker::class.java.simpleName)
    }
}