package dev.sobhy.babycareassistant.alarm.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dev.sobhy.babycareassistant.alarm.notification.FeedingNotificationReceiver
import dev.sobhy.babycareassistant.alarm.notification.NotificationReceiver
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.alarm.data.FeedingSchedule
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

object AlarmManagerHelper {
    fun scheduleVaccinationAlarm(context: Context, vaccination: Vaccination) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("type", "vaccination")
            putExtra("data", vaccination)
        }
        val vaccinationDate = LocalDate.parse(vaccination.date)
        val timeTemp = if (vaccinationDate == LocalDate.now()) {
            vaccinationDate.atTime(LocalTime.now().plusSeconds(10))
        } else {
            vaccinationDate.atTime(LocalTime.of(10, 0))
        }

        val alarmTime = timeTemp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        scheduleAlarm(context, intent, alarmTime, vaccination.id.hashCode())
    }

    fun scheduleDiaperChangeAlarm(context: Context, diaperChange: Diapers, timeIndex: Int) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("type", "diaper")
            putExtra("data", diaperChange)
            putExtra("timeIndex", timeIndex)
        }
        val dateTemp = LocalDate.parse(diaperChange.date)
        val timeTemp = LocalTime.parse(diaperChange.timesOfDiapersChange[timeIndex])
        val dateTimeTemp = LocalDateTime.of(dateTemp, timeTemp)
        val alarmTime = dateTimeTemp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        scheduleAlarm(context, intent, alarmTime, diaperChange.id.hashCode() + timeIndex)
    }

    private fun scheduleAlarm(context: Context, intent: Intent, time: Long, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("AlarmManagerHelper", "scheduleAlarm: $time")
        if (System.currentTimeMillis() > time) {
            return
        }
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun scheduleFeedingNotification(
        context: Context,
        feedingSchedule: FeedingSchedule,
        feedingIntervalHours: Int,
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, FeedingNotificationReceiver::class.java).apply {
            putExtra("feedingSchedule", feedingSchedule)
            putExtra("feedingIntervalHours", feedingIntervalHours)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val time =
            if (feedingIntervalHours == 0) LocalTime.now().plusSeconds(10) else LocalTime.now()
                .plusHours(feedingIntervalHours.toLong())
        Log.d("AlarmManagerHelper", "scheduleNextFeedingNotification: $time")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time.toSecondOfDay().toLong() * 1000,
            pendingIntent
        )
    }
}