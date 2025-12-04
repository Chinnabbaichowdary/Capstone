package com.chorepal.app.data.repository

import com.chorepal.app.data.dao.NotificationDao
import com.chorepal.app.data.models.AppNotification
import kotlinx.coroutines.flow.Flow

class NotificationRepository(private val notificationDao: NotificationDao) {
    
    fun getNotificationsByUserId(userId: String): Flow<List<AppNotification>> = 
        notificationDao.getNotificationsByUserId(userId)
    
    fun getUnreadNotifications(userId: String): Flow<List<AppNotification>> = 
        notificationDao.getUnreadNotifications(userId)
    
    fun getUnreadNotificationCount(userId: String): Flow<Int> = 
        notificationDao.getUnreadNotificationCount(userId)
    
    suspend fun insertNotification(notification: AppNotification) = 
        notificationDao.insertNotification(notification)
    
    suspend fun updateNotification(notification: AppNotification) = 
        notificationDao.updateNotification(notification)
    
    suspend fun markAsRead(notificationId: String) = 
        notificationDao.markAsRead(notificationId)
    
    suspend fun markAllAsRead(userId: String) = 
        notificationDao.markAllAsRead(userId)
    
    suspend fun deleteNotification(notification: AppNotification) = 
        notificationDao.deleteNotification(notification)
    
    suspend fun deleteAllNotifications(userId: String) = 
        notificationDao.deleteAllNotifications(userId)
}

