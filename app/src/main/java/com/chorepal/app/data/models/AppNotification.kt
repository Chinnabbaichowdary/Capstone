package com.chorepal.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class AppNotification(
    @PrimaryKey val notificationId: String,
    val userId: String, // Recipient
    val title: String,
    val message: String,
    val notificationType: NotificationType,
    val relatedChoreId: String? = null,
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val actionUrl: String? = null
)

enum class NotificationType {
    CHORE_ASSIGNED,
    CHORE_COMPLETED,
    CHORE_APPROVED,
    CHORE_REJECTED,
    POINTS_EARNED,
    POINTS_REDEEMED,
    REMINDER,
    GENERAL
}

