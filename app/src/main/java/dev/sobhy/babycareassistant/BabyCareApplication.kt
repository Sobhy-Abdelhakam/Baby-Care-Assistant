package dev.sobhy.babycareassistant

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import dev.sobhy.babycareassistant.utils.NOTIFICATION_CHANNEL_DESCRIPTION
import dev.sobhy.babycareassistant.utils.NOTIFICATION_CHANNEL_ID
import dev.sobhy.babycareassistant.utils.NOTIFICATION_CHANNEL_NAME

@HiltAndroidApp
class BabyCareApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = NOTIFICATION_CHANNEL_DESCRIPTION
        }
        notificationManager.createNotificationChannel(channel)
    }
}