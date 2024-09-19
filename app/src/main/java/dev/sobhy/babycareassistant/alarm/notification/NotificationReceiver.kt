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
import dagger.hilt.android.AndroidEntryPoint
import dev.sobhy.babycareassistant.MainActivity
import dev.sobhy.babycareassistant.R
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.notification.data.repository.NotificationRepository
import dev.sobhy.babycareassistant.notification.domain.NotificationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class NotificationReceiver: BroadcastReceiver() {
    @Inject lateinit var notificationRepository: NotificationRepository
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("title") ?: "No title"
        val message = intent?.getStringExtra("message")?: "No message"
        val notificationEntity = NotificationEntity(
            title = title,
            message = message,
            timestamp = System.currentTimeMillis()
        )
        // store notification in database
        CoroutineScope(Dispatchers.IO).launch {
            notificationRepository.saveNotification(notificationEntity)
        }
        val feedingData = intent?.getParcelableExtra<BreastFeed>("feedingData")
        val diaperData = intent?.getParcelableExtra<Diapers>("diaperData")
        val vaccinationData = intent?.getParcelableExtra<Diapers>("vaccinationData")

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
                } else if (diaperData != null) {
                    putExtra("diaperData", diaperData).apply {
                        Log.d("Notification", "send data success $diaperData")
                    }
                } else if (vaccinationData != null) {
                    putExtra("vaccinationData", vaccinationData).apply {
                        Log.d("Notification", "send data success $vaccinationData")
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