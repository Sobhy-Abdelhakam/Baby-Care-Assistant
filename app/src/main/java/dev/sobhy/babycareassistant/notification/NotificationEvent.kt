package dev.sobhy.babycareassistant.notification

sealed class NotificationEvent {
    object FetchNotifications : NotificationEvent()
}