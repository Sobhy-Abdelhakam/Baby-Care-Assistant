package dev.sobhy.babycareassistant.notification

import dev.sobhy.babycareassistant.notification.domain.NotificationEntity

sealed class NotificationState {
    object Loading : NotificationState()
    data class Success(val notifications: List<NotificationEntity>) : NotificationState()
    data class Error(val message: String) : NotificationState()
}
