package dev.sobhy.babycareassistant.alarm.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.sobhy.babycareassistant.NotificationActivity
import dev.sobhy.babycareassistant.R
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.notification.data.repository.NotificationRepository
import dev.sobhy.babycareassistant.notification.domain.NotificationEntity
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationRepository: NotificationRepository
    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra("type")
        Log.d("NotificationReceiver", "onReceive: $type")
        when (type) {
            "vaccination" -> {
                val vaccination = intent.getParcelableExtra<Vaccination>("data")
                vaccination?.let {
                    storeNotificationInDatabase(
                        "It's time to vaccination",
                        "${vaccination.name} ${vaccination.code}"
                    )
                    sendNotification(context, "It's time for vaccination: ${vaccination.name}", it)
                }
            }

            "diaper" -> {
                val diaper = intent.getParcelableExtra<Diapers>("data")
                val timeIndex = intent.getIntExtra("timeIndex", 0)
                diaper?.let {
                    storeNotificationInDatabase(
                        "It's time to change diaper",
                        "time: ${timeIndex + 1}: ${it.timesOfDiapersChange[timeIndex]}"
                    )
                    sendNotification(
                        context,
                        "It's time for diaper change number: ${timeIndex + 1}",
                        it,
                        timeIndex
                    )
                }
            }
        }
    }

    private fun storeNotificationInDatabase(title: String, message: String) {
        val notificationEntity = NotificationEntity(
            title = title,
            message = message,
            timestamp = System.currentTimeMillis()
        )
        CoroutineScope(Dispatchers.IO).launch {
            notificationRepository.saveNotification(notificationEntity)
        }
    }

    private fun sendNotification(
        context: Context,
        content: String,
        data: Parcelable,
        timeIndex: Int? = null,
    ) {
        Log.d("NotificationReceiver", "sendNotification: $data")
        val notificationIntent = Intent(context, NotificationActivity::class.java).apply {
            putExtra("notificationData", data)
            putExtra("timeIndex", timeIndex)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, "baby_care_channel")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Baby Care Assistant")
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(content.hashCode(), notification)
    }
}