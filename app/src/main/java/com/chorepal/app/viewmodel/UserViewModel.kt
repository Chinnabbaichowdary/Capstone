package com.chorepal.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chorepal.app.data.models.PointTransaction
import com.chorepal.app.data.models.TransactionType
import com.chorepal.app.data.models.User
import com.chorepal.app.data.repository.PointRepository
import com.chorepal.app.data.repository.UserRepository
import com.chorepal.app.notifications.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class UserViewModel(
    private val userRepository: UserRepository,
    private val pointRepository: PointRepository,
    private val notificationHelper: NotificationHelper
) : ViewModel() {
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _children = MutableStateFlow<List<User>>(emptyList())
    val children: StateFlow<List<User>> = _children.asStateFlow()
    
    private val _pointTransactions = MutableStateFlow<List<PointTransaction>>(emptyList())
    val pointTransactions: StateFlow<List<PointTransaction>> = _pointTransactions.asStateFlow()
    
    private val _userState = MutableStateFlow<UserState>(UserState.Idle)
    val userState: StateFlow<UserState> = _userState.asStateFlow()
    
    fun loadUser(userId: String) {
        viewModelScope.launch {
            userRepository.getUserById(userId).collect { user ->
                _currentUser.value = user
            }
        }
    }
    
    fun loadChildren(parentId: String) {
        viewModelScope.launch {
            userRepository.getChildrenByParentId(parentId).collect { children ->
                _children.value = children
            }
        }
    }
    
    fun loadPointTransactions(userId: String) {
        viewModelScope.launch {
            pointRepository.getTransactionsByUserId(userId).collect { transactions ->
                _pointTransactions.value = transactions
            }
        }
    }
    
    fun redeemPoints(childId: String, points: Int, reason: String, parentId: String) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val child = userRepository.getUserByIdSync(childId)
                if (child != null && child.totalPoints >= points) {
                    val transaction = PointTransaction(
                        transactionId = UUID.randomUUID().toString(),
                        userId = childId,
                        choreId = null,
                        pointsChange = -points,
                        transactionType = TransactionType.REDEEMED,
                        reason = reason,
                        performedBy = parentId
                    )
                    
                    pointRepository.insertTransaction(transaction)
                    notificationHelper.sendPointsRedeemedNotification(childId, points, reason, parentId)
                    
                    _userState.value = UserState.Success("Points redeemed successfully!")
                } else {
                    _userState.value = UserState.Error("Insufficient points")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Failed to redeem points")
            }
        }
    }
    
    fun addBonusPoints(childId: String, points: Int, reason: String, parentId: String) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val transaction = PointTransaction(
                    transactionId = UUID.randomUUID().toString(),
                    userId = childId,
                    choreId = null,
                    pointsChange = points,
                    transactionType = TransactionType.BONUS,
                    reason = reason,
                    performedBy = parentId
                )
                
                pointRepository.insertTransaction(transaction)
                _userState.value = UserState.Success("Bonus points added!")
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Failed to add bonus points")
            }
        }
    }
    
    fun adjustPoints(childId: String, points: Int, reason: String, parentId: String) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val transaction = PointTransaction(
                    transactionId = UUID.randomUUID().toString(),
                    userId = childId,
                    choreId = null,
                    pointsChange = points,
                    transactionType = TransactionType.ADJUSTMENT,
                    reason = reason,
                    performedBy = parentId
                )
                
                pointRepository.insertTransaction(transaction)
                _userState.value = UserState.Success("Points adjusted!")
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Failed to adjust points")
            }
        }
    }
    
    fun resetState() {
        _userState.value = UserState.Idle
    }
}

sealed class UserState {
    object Idle : UserState()
    object Loading : UserState()
    data class Success(val message: String) : UserState()
    data class Error(val message: String) : UserState()
}

