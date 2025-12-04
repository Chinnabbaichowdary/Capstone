package com.chorepal.app.notifications

import com.chorepal.app.data.models.AppNotification
import com.chorepal.app.data.models.Chore
import com.chorepal.app.data.models.NotificationType
import com.chorepal.app.data.models.User
import com.chorepal.app.data.repository.NotificationRepository
import com.chorepal.app.data.repository.UserRepository
import java.util.UUID

class NotificationHelper(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository
) {
    
    private val emailService = EmailNotificationService()
    
    suspend fun sendChoreAssignedNotification(userId: String, chore: Chore) {
        val notification = AppNotification(
            notificationId = UUID.randomUUID().toString(),
            userId = userId,
            title = "New Chore Assigned!",
            message = "You have a new chore: ${chore.title}",
            notificationType = NotificationType.CHORE_ASSIGNED,
            relatedChoreId = chore.choreId
        )
        notificationRepository.insertNotification(notification)
        
        // Send email notification
        try {
            val child = userRepository.getUserByIdSync(userId)
            val parent = userRepository.getUserByIdSync(chore.createdBy)
            if (child != null && parent != null) {
                emailService.sendChoreAssignedEmail(child, chore, parent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    suspend fun sendChoreCompletedNotification(parentId: String, chore: Chore, childName: String) {
        val notification = AppNotification(
            notificationId = UUID.randomUUID().toString(),
            userId = parentId,
            title = "Chore Completed!",
            message = "$childName has completed: ${chore.title}",
            notificationType = NotificationType.CHORE_COMPLETED,
            relatedChoreId = chore.choreId
        )
        notificationRepository.insertNotification(notification)
        
        // Send email notification
        try {
            val parent = userRepository.getUserByIdSync(parentId)
            val child = chore.assignedTo?.let { userRepository.getUserByIdSync(it) }
            if (parent != null && child != null) {
                emailService.sendChoreCompletedEmail(parent, chore, child)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    suspend fun sendChoreApprovedNotification(childId: String, chore: Chore) {
        val notification = AppNotification(
            notificationId = UUID.randomUUID().toString(),
            userId = childId,
            title = "Chore Approved! 🎉",
            message = "Your chore '${chore.title}' was approved! You earned ${chore.pointsValue} points!",
            notificationType = NotificationType.CHORE_APPROVED,
            relatedChoreId = chore.choreId
        )
        notificationRepository.insertNotification(notification)
        
        // Send email notification
        try {
            val child = userRepository.getUserByIdSync(childId)
            val parent = userRepository.getUserByIdSync(chore.createdBy)
            if (child != null && parent != null) {
                emailService.sendChoreApprovedEmail(child, chore, parent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    suspend fun sendChoreRejectedNotification(childId: String, chore: Chore, reason: String?) {
        val notification = AppNotification(
            notificationId = UUID.randomUUID().toString(),
            userId = childId,
            title = "Chore Needs Revision",
            message = "Your chore '${chore.title}' needs to be redone. ${reason ?: ""}",
            notificationType = NotificationType.CHORE_REJECTED,
            relatedChoreId = chore.choreId
        )
        notificationRepository.insertNotification(notification)
        
        // Send email notification
        try {
            val child = userRepository.getUserByIdSync(childId)
            val parent = userRepository.getUserByIdSync(chore.createdBy)
            if (child != null && parent != null) {
                emailService.sendChoreRejectedEmail(child, chore, parent, reason)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    suspend fun sendPointsRedeemedNotification(userId: String, points: Int, reason: String, parentId: String) {
        val notification = AppNotification(
            notificationId = UUID.randomUUID().toString(),
            userId = userId,
            title = "Points Redeemed",
            message = "$points points were used for: $reason",
            notificationType = NotificationType.POINTS_REDEEMED
        )
        notificationRepository.insertNotification(notification)
        
        // Send email notification
        try {
            val child = userRepository.getUserByIdSync(userId)
            val parent = userRepository.getUserByIdSync(parentId)
            if (child != null && parent != null) {
                emailService.sendPointsRedeemedEmail(child, points, reason, parent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

