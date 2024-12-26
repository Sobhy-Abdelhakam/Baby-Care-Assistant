package dev.sobhy.babycareassistant.notification

import dev.sobhy.babycareassistant.notification.domain.Notification

sealed class NotificationState {
    object Loading : NotificationState()
    data class Success(val notifications: List<Notification>) : NotificationState()
    data class Error(val message: String) : NotificationState()
}
