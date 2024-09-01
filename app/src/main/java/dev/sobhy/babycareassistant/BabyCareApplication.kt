package dev.sobhy.babycareassistant

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BabyCareApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "default",
            "feeding and diapers Reminder",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for breastfeeding and diapers reminder notifications"
        }
        notificationManager.createNotificationChannel(channel)
    }
}