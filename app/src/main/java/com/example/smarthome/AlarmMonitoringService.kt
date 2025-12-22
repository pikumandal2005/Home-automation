package com.example.smarthome

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Foreground Service for monitoring fire and fault alarms even when app is in background
 * This ensures users receive critical safety notifications immediately
 */
class AlarmMonitoringService : Service() {

    companion object {
        private const val TAG = "AlarmMonitorService"
        private const val CHANNEL_ID = "alarm_monitoring_channel"
        private const val FIRE_ALARM_CHANNEL_ID = "fire_alarm_channel"
        private const val FAULT_ALARM_CHANNEL_ID = "fault_alarm_channel"
        private const val NOTIFICATION_ID = 1001
        private const val FIRE_ALARM_NOTIFICATION_ID = 2001
        private const val FAULT_ALARM_NOTIFICATION_ID = 3001
        
        const val ACTION_START_MONITORING = "com.example.smarthome.START_MONITORING"
        const val ACTION_STOP_MONITORING = "com.example.smarthome.STOP_MONITORING"
        const val ACTION_FIRE_ALARM = "com.example.smarthome.FIRE_ALARM"
        const val ACTION_FAULT_ALARM = "com.example.smarthome.FAULT_ALARM"
        const val ACTION_ALARM_CLEARED = "com.example.smarthome.ALARM_CLEARED"
    }

    private var wakeLock: PowerManager.WakeLock? = null
    private var isFireAlarmActive = false
    private var isFaultAlarmActive = false

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        createNotificationChannels()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_MONITORING -> {
                startForegroundService()
            }
            ACTION_STOP_MONITORING -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
            ACTION_FIRE_ALARM -> {
                handleFireAlarm(true)
            }
            ACTION_FAULT_ALARM -> {
                handleFaultAlarm(true)
            }
            ACTION_ALARM_CLEARED -> {
                val alarmType = intent.getStringExtra("alarm_type")
                when (alarmType) {
                    "fire" -> handleFireAlarm(false)
                    "fault" -> handleFaultAlarm(false)
                }
            }
        }
        return START_STICKY
    }

    private fun startForegroundService() {
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Smart Home Monitoring")
            .setContentText("Monitoring for fire and fault alarms")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(NOTIFICATION_ID, notification)
        Log.d(TAG, "Foreground service started")
    }

    private fun handleFireAlarm(isActive: Boolean) {
        isFireAlarmActive = isActive
        
        if (isActive) {
            acquireWakeLock()
            showCriticalAlarmNotification(
                "🔥 FIRE ALARM! 🔥",
                "CRITICAL: Fire detected in your home! Take immediate action!",
                FIRE_ALARM_NOTIFICATION_ID,
                FIRE_ALARM_CHANNEL_ID
            )
            Log.e(TAG, "FIRE ALARM TRIGGERED")
        } else {
            releaseWakeLock()
            clearNotification(FIRE_ALARM_NOTIFICATION_ID)
            Log.d(TAG, "Fire alarm cleared")
        }
    }

    private fun handleFaultAlarm(isActive: Boolean) {
        isFaultAlarmActive = isActive
        
        if (isActive) {
            acquireWakeLock()
            showCriticalAlarmNotification(
                "⚠️ FAULT ALERT ⚠️",
                "Warning: Electrical fault detected in your home system!",
                FAULT_ALARM_NOTIFICATION_ID,
                FAULT_ALARM_CHANNEL_ID
            )
            Log.w(TAG, "FAULT ALARM TRIGGERED")
        } else {
            releaseWakeLock()
            clearNotification(FAULT_ALARM_NOTIFICATION_ID)
            Log.d(TAG, "Fault alarm cleared")
        }
    }

    private fun showCriticalAlarmNotification(title: String, message: String, notificationId: Int, channelId: String) {
        val fullScreenIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            this, notificationId, fullScreenIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(false)
            .setOngoing(true)
            .setVibrate(longArrayOf(0, 1000, 500, 1000, 500, 1000))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setContentIntent(fullScreenPendingIntent)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)
        try {
            notificationManager.notify(notificationId, notification)
        } catch (e: SecurityException) {
            Log.e(TAG, "Notification permission not granted", e)
        }
    }

    private fun clearNotification(notificationId: Int) {
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.cancel(notificationId)
    }

    private fun acquireWakeLock() {
        if (wakeLock == null) {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "SmartHome::AlarmWakeLock"
            )
        }
        if (wakeLock?.isHeld == false) {
            wakeLock?.acquire(10 * 60 * 1000L) // 10 minutes max
            Log.d(TAG, "WakeLock acquired")
        }
    }

    private fun releaseWakeLock() {
        if (!isFireAlarmActive && !isFaultAlarmActive) {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                    Log.d(TAG, "WakeLock released")
                }
            }
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            // Foreground service channel (low priority)
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Alarm Monitoring Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Background monitoring for smart home alarms"
                setShowBadge(false)
            }

            // Fire alarm channel (critical priority)
            val fireChannel = NotificationChannel(
                FIRE_ALARM_CHANNEL_ID,
                "Fire Alarm Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical fire alarm notifications"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 1000, 500, 1000, 500, 1000)
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                enableLights(true)
                lightColor = android.graphics.Color.RED
                setShowBadge(true)
            }

            // Fault alarm channel (high priority)
            val faultChannel = NotificationChannel(
                FAULT_ALARM_CHANNEL_ID,
                "Fault Alarm Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Electrical fault alarm notifications"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500)
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                enableLights(true)
                lightColor = android.graphics.Color.YELLOW
                setShowBadge(true)
            }

            notificationManager.createNotificationChannel(serviceChannel)
            notificationManager.createNotificationChannel(fireChannel)
            notificationManager.createNotificationChannel(faultChannel)
            
            Log.d(TAG, "Notification channels created")
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        releaseWakeLock()
        Log.d(TAG, "Service destroyed")
    }
}
