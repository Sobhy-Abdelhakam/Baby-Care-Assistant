package dev.sobhy.babycareassistant.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.sobhy.babycareassistant.R
import dev.sobhy.babycareassistant.alarm.data.FeedingSchedule
import dev.sobhy.babycareassistant.alarm.notification.NotificationReceiver
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

fun calculateBabyAgeInMonths(birthDate: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val birthDateParsed = LocalDate.parse(birthDate, formatter)
    val now = LocalDate.now()

    return Period.between(birthDateParsed, now).months + Period.between(birthDateParsed, now).years * 12
}

fun getFeedingDataForAge(context: Context, ageInMonths: Int): FeedingSchedule? {
    val json = context.resources.openRawResource(R.raw.feeding_schedule).bufferedReader().use {
        it.readText()
    }
    val type = object : TypeToken<Map<String, FeedingSchedule>>() {}.type
    val feedingData: Map<String, FeedingSchedule> = Gson().fromJson(json, type)

    return when (ageInMonths) {
        1 -> feedingData["month_1"]
        2 -> feedingData["month_2"]
        in 3..4 -> feedingData["month_3_4"]
        in 5..6 -> feedingData["month_5_6"]
        in 7..9 -> feedingData["month_7_9"]
        in 10..12 -> feedingData["month_10_12"]
        else -> null
    }
}

fun scheduleNextNotification(context: Context, intervalNotes: String) {
    val intervalInMillis = when (intervalNotes) {
        "Every 2 hours" -> 2 * 60 * 60 * 1000L
        "Every 3 hours" -> 3 * 60 * 60 * 1000L
        "Every 4 hours" -> 4 * 60 * 60 * 1000L
        "Every 5 hours" -> 5 * 60 * 60 * 1000L
        "Every 6 hours" -> 6 * 60 * 60 * 1000L
        else -> throw IllegalArgumentException("Unsupported interval")
    }

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("type", TYPE_FEEDING)
        putExtra("title", "Breastfeeding Reminder")
        putExtra("message", "It's time to breastfeed your baby.")
    }
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    val triggerTime = System.currentTimeMillis() + intervalInMillis

    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
}
