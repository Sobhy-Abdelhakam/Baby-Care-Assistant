package dev.sobhy.babycareassistant.notification.domain

data class Notification(
    val title: String = "",
    val message: String = "",
    val date: Long = 0,
)
