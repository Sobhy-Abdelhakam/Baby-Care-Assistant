package dev.sobhy.babycareassistant.notification.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.sobhy.babycareassistant.notification.domain.NotificationEntity

@Database(entities = [NotificationEntity::class], version = 1)
abstract class NotificationDatabase: RoomDatabase() {
    abstract fun notificationDao(): NotificationDao

}