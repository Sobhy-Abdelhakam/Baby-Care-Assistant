package dev.sobhy.babycareassistant.alarm.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dev.sobhy.babycareassistant.alarm.notification.NotificationReceiver
import dev.sobhy.babycareassistant.diapers.data.model.Diapers
import dev.sobhy.babycareassistant.utils.DEFAULT_FEEDING_TITLE
import dev.sobhy.babycareassistant.utils.TYPE_DIAPER
import dev.sobhy.babycareassistant.utils.TYPE_FEEDING
import dev.sobhy.babycareassistant.utils.TYPE_VACCINATION
import dev.sobhy.babycareassistant.vaccination.data.Vaccination
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class AlarmManagerHelper(private val context: Context) {
    fun scheduleVaccinationAlarm(vaccination: Vaccination) {
        val pendingIntent = createAlarmIntent(vaccination.id.hashCode(), TYPE_VACCINATION) {
            putExtra("data", vaccination)
        }
        val vaccinationDate = LocalDate.parse(vaccination.date)
        val time = if (vaccinationDate == LocalDate.now()) {
            vaccinationDate.atTime(LocalTime.now().plusSeconds(10))
        } else {
            vaccinationDate.atTime(LocalTime.of(10, 0))
        }

        val alarmTime = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        scheduleAlarm(pendingIntent, alarmTime)
    }

    fun scheduleDiaperChangeAlarm(diaperChange: Diapers, timeIndex: Int) {
        val pendingIntent = createAlarmIntent(diaperChange.id.hashCode() + timeIndex, TYPE_DIAPER) {
            putExtra("data", diaperChange)
            putExtra("timeIndex", timeIndex)
        }
        val date = LocalDate.parse(diaperChange.date)
        val time = LocalTime.parse(diaperChange.timesOfDiapersChange[timeIndex])
        val dateTime = LocalDateTime.of(date, time)
        val alarmTime = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        scheduleAlarm(pendingIntent, alarmTime)
    }

    fun initialFeedingNotification() {
        val pendingIntent = createAlarmIntent(0, TYPE_FEEDING) {
            putExtra("title", DEFAULT_FEEDING_TITLE)
            putExtra("message", "It's time to breastfeed your baby.")
        }
        val triggerTime = System.currentTimeMillis() + 10_000 // 10 seconds later
        scheduleAlarm(pendingIntent, triggerTime)
    }

    private fun scheduleAlarm(pendingIntent: PendingIntent, time: Long) {
        if (System.currentTimeMillis() > time) return // Skip past alarms
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d("AlarmManagerHelper", "scheduleAlarm: $time")
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun createAlarmIntent(
        requestCode: Int,
        type: String,
        putExtras: Intent.() -> Unit,
    ): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("type", type)
            putExtras()
        }

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}