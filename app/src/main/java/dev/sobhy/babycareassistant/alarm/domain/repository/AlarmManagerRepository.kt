package dev.sobhy.babycareassistant.alarm.domain.repository

import dev.sobhy.babycareassistant.alarm.domain.model.AlarmData

interface AlarmManagerRepository {
    fun <T> scheduleAlarm(alarmData: AlarmData<T>)
    fun cancelAlarm(alarmId: String)
}