package dev.sobhy.babycareassistant.alarm.domain.model


data class AlarmData<T>(
    val id: String,
    val title: String,
    val message: String,
    val data: T,
    val timeInMillis: Long,
)
