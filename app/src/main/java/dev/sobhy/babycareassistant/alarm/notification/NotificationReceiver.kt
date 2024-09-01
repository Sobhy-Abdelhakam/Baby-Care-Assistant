package dev.sobhy.babycareassistant.alarm.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import dev.sobhy.babycareassistant.MainActivity
import dev.sobhy.babycareassistant.R
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import kotlin.random.Random

class NotificationReceiver: BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("title")
        val message = intent?.getStringExtra("message")
        val feedingData = intent?.getParcelableExtra<BreastFeed>("feedingData")
        val diaperData = intent?.getParcelableExtra<Diapers>("diaperData")
        context?.let {ctx ->
            val notificationIntent = Intent(ctx, MainActivity::class.java).apply {
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
                putExtra("NotificationTitle", title)
                putExtra("NotificationMessage", message)
                if (feedingData != null){
                    putExtra("feedingData", feedingData).apply {
                        Log.d("Notification", "send data success $feedingData")
                    }
                } else if (diaperData != null){
                    putExtra("diaperData", diaperData).apply {
                        Log.d("Notification", "send data success $diaperData")
                    }
                }

            }
            val pendingIntent = PendingIntent.getActivity(
                ctx,
                1,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(ctx, "default")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
            Log.d("NotificationReceiver", "onReceive: $title")
            notificationManager.notify(Random.nextInt(), builder.build())
        }





    }
}