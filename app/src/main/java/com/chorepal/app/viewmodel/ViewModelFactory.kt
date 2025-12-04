package com.chorepal.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chorepal.app.auth.AuthManager
import com.chorepal.app.data.database.ChorePalDatabase
import com.chorepal.app.data.repository.*
import com.chorepal.app.notifications.NotificationHelper

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    
    private val database = ChorePalDatabase.getDatabase(context)
    
    private val userRepository = UserRepository(database.userDao())
    private val choreRepository = ChoreRepository(database.choreDao())
    private val pointRepository = PointRepository(database.pointTransactionDao(), database.userDao())
    private val notificationRepository = NotificationRepository(database.notificationDao())
    
    private val authManager = AuthManager(context, userRepository)
    private val notificationHelper = NotificationHelper(notificationRepository, userRepository)
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authManager) as T
            }
            modelClass.isAssignableFrom(ChoreViewModel::class.java) -> {
                ChoreViewModel(choreRepository, pointRepository, userRepository, notificationHelper) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(userRepository, pointRepository, notificationHelper) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

