package dev.sobhy.babycareassistant.alarm.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import dev.sobhy.babycareassistant.NotificationActivity
import dev.sobhy.babycareassistant.R
import dev.sobhy.babycareassistant.alarm.data.FeedingSchedule

class FeedingNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("FeedingNotificationReceiver", "Feeding notification received")
        val feedingSchedule = intent.getParcelableExtra<FeedingSchedule>("feedingSchedule")
        val notificationIntent = Intent(context, NotificationActivity::class.java).apply {
            putExtra("feedingSchedule", feedingSchedule)
        }
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        val notification = NotificationCompat.Builder(context, "baby_care_channel")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Time to feed your baby!")
            .setContentText("It's time for the next feeding.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

//        NotificationManagerCompat.from(context).notify(1, notification)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
        // Schedule the next feeding
//        val feedingInterval = intent.getIntExtra("feeding_interval", 3)
//        AlarmManagerHelper.scheduleNextFeedingNotification(context, feedingInterval)
    }
}