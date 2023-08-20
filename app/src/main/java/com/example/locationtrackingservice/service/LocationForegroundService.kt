package com.example.locationtrackingservice.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build.*
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.locationtrackingservice.*
import com.example.locationtrackingservice.R
import com.example.locationtrackingservice.worker.SaveLocationWorker

class LocationForegroundService : Service() {

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
        enqueueOneTimeWorkRequest()

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelPeriodicWorkRequest()
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

    private fun enqueueOneTimeWorkRequest() {
        val saveLocationRequest = OneTimeWorkRequestBuilder<SaveLocationWorker>()
            .build()

        WorkManager.getInstance(application).enqueue(saveLocationRequest)
        Log.d(LOG_TAG_WORKER, "WORK REQUEST: $saveLocationRequest")
    }

    private fun cancelPeriodicWorkRequest() {
        WorkManager.getInstance(applicationContext).cancelAllWork()
    }
}