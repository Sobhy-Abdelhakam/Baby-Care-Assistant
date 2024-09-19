package dev.sobhy.babycareassistant.alarm.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dev.sobhy.babycareassistant.alarm.domain.model.AlarmData
import dev.sobhy.babycareassistant.alarm.domain.repository.AlarmManagerRepository
import dev.sobhy.babycareassistant.alarm.notification.NotificationReceiver
import dev.sobhy.babycareassistant.breastfeeding.data.model.BreastFeed
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.vaccination.data.Vaccination

class AlarmManagerRepositoryImpl(
    private val context: Context,
) : AlarmManagerRepository {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    override fun <T> scheduleAlarm(alarmData: AlarmData<T>) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", alarmData.title)
            putExtra("message", alarmData.message)

            if (alarmData.data is BreastFeed)
                putExtra("feedingData", alarmData.data as BreastFeed)
            else if (alarmData.data is Diapers)
                putExtra("diaperData", alarmData.data as Diapers)
            else if (alarmData.data is Vaccination)
                putExtra("vaccinationData", alarmData.data as Vaccination)

        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmData.timeInMillis.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("TAG", "scheduleAlarm: ${alarmData.timeInMillis}")
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmData.timeInMillis, pendingIntent)
    }

    override fun cancelAlarm(alarmId: String) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}