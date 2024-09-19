package dev.sobhy.babycareassistant.notification.data.repository

import dev.sobhy.babycareassistant.notification.data.local.NotificationDao
import dev.sobhy.babycareassistant.notification.domain.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationRepository(
    private val notificationDao: NotificationDao
) {
    suspend fun saveNotification(notification: NotificationEntity){
        notificationDao.saveNotification(notification)
    }
    fun getNotifications(): Flow<List<NotificationEntity>> {
        return notificationDao.getAllNotifications()
    }
}