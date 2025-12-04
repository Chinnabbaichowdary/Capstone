package com.chorepal.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chorepal.app.data.models.*
import com.chorepal.app.data.repository.ChoreRepository
import com.chorepal.app.data.repository.PointRepository
import com.chorepal.app.data.repository.UserRepository
import com.chorepal.app.notifications.NotificationHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class ChoreViewModel(
    private val choreRepository: ChoreRepository,
    private val pointRepository: PointRepository,
    private val userRepository: UserRepository,
    private val notificationHelper: NotificationHelper
) : ViewModel() {
    
    private val _chores = MutableStateFlow<List<Chore>>(emptyList())
    val chores: StateFlow<List<Chore>> = _chores.asStateFlow()
    
    private val _pendingChores = MutableStateFlow<List<Chore>>(emptyList())
    val pendingChores: StateFlow<List<Chore>> = _pendingChores.asStateFlow()
    
    private val _choreState = MutableStateFlow<ChoreState>(ChoreState.Idle)
    val choreState: StateFlow<ChoreState> = _choreState.asStateFlow()
    
    fun loadChoresForChild(childId: String) {
        viewModelScope.launch {
            choreRepository.getChoresByChildId(childId).collect { chores ->
                _chores.value = chores
            }
        }
    }
    
    fun loadChoresForParent(parentId: String) {
        viewModelScope.launch {
            choreRepository.getChoresByParentId(parentId).collect { chores ->
                _chores.value = chores
            }
        }
    }
    
    fun loadPendingApprovals(parentId: String) {
        viewModelScope.launch {
            choreRepository.getPendingApprovalChores(parentId).collect { chores ->
                _pendingChores.value = chores
            }
        }
    }
    
    fun createChore(
        title: String,
        description: String,
        pointsValue: Int,
        choreType: ChoreType,
        createdBy: String,
        assignedTo: String?,
        dueDate: Long? = null,
        isRecurring: Boolean = false,
        recurringFrequency: RecurringFrequency? = null
    ) {
        viewModelScope.launch {
            _choreState.value = ChoreState.Loading
            try {
                val chore = Chore(
                    choreId = UUID.randomUUID().toString(),
                    title = title,
                    description = description,
                    pointsValue = pointsValue,
                    choreType = choreType,
                    createdBy = createdBy,
                    assignedTo = assignedTo,
                    status = ChoreStatus.ASSIGNED,
                    dueDate = dueDate,
                    isRecurring = isRecurring,
                    recurringFrequency = recurringFrequency
                )
                
                choreRepository.insertChore(chore)
                
                // Send notification to child
                assignedTo?.let {
                    notificationHelper.sendChoreAssignedNotification(it, chore)
                }
                
                _choreState.value = ChoreState.Success("Chore created successfully!")
            } catch (e: Exception) {
                _choreState.value = ChoreState.Error(e.message ?: "Failed to create chore")
            }
        }
    }
    
    fun markChoreAsCompleted(choreId: String, childId: String, imageProofPath: String? = null) {
        viewModelScope.launch {
            _choreState.value = ChoreState.Loading
            try {
                val chore = choreRepository.getChoreByIdSync(choreId)
                if (chore != null) {
                    // Update chore with image proof if provided
                    val updatedChore = chore.copy(
                        status = ChoreStatus.COMPLETED_PENDING_APPROVAL,
                        completedAt = System.currentTimeMillis(),
                        imageProofUrl = imageProofPath ?: chore.imageProofUrl
                    )
                    choreRepository.updateChore(updatedChore)
                    
                    // Notify parent
                    val child = userRepository.getUserByIdSync(childId)
                    notificationHelper.sendChoreCompletedNotification(
                        chore.createdBy,
                        chore,
                        child?.name ?: "Your child"
                    )
                    
                    _choreState.value = ChoreState.Success("Chore marked as completed!")
                } else {
                    _choreState.value = ChoreState.Error("Chore not found")
                }
            } catch (e: Exception) {
                _choreState.value = ChoreState.Error(e.message ?: "Failed to update chore")
            }
        }
    }
    
    fun updateChoreImage(choreId: String, imagePath: String) {
        viewModelScope.launch {
            try {
                val chore = choreRepository.getChoreByIdSync(choreId)
                chore?.let {
                    val updated = it.copy(imageProofUrl = imagePath)
                    choreRepository.updateChore(updated)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun approveChore(choreId: String) {
        viewModelScope.launch {
            _choreState.value = ChoreState.Loading
            try {
                val chore = choreRepository.getChoreByIdSync(choreId)
                if (chore != null && chore.assignedTo != null) {
                    // Update chore status
                    val updatedChore = chore.copy(
                        status = ChoreStatus.APPROVED,
                        verifiedAt = System.currentTimeMillis()
                    )
                    choreRepository.updateChore(updatedChore)
                    
                    // Award points
                    val transaction = PointTransaction(
                        transactionId = UUID.randomUUID().toString(),
                        userId = chore.assignedTo,
                        choreId = choreId,
                        pointsChange = chore.pointsValue,
                        transactionType = TransactionType.EARNED,
                        reason = "Completed: ${chore.title}",
                        performedBy = chore.createdBy
                    )
                    pointRepository.insertTransaction(transaction)
                    
                    // Notify child
                    notificationHelper.sendChoreApprovedNotification(chore.assignedTo, chore)
                    
                    _choreState.value = ChoreState.Success("Chore approved!")
                } else {
                    _choreState.value = ChoreState.Error("Chore not found")
                }
            } catch (e: Exception) {
                _choreState.value = ChoreState.Error(e.message ?: "Failed to approve chore")
            }
        }
    }
    
    fun rejectChore(choreId: String, reason: String?) {
        viewModelScope.launch {
            _choreState.value = ChoreState.Loading
            try {
                val chore = choreRepository.getChoreByIdSync(choreId)
                if (chore != null && chore.assignedTo != null) {
                    val updatedChore = chore.copy(
                        status = ChoreStatus.REJECTED,
                        notes = reason
                    )
                    choreRepository.updateChore(updatedChore)
                    
                    // Notify child
                    notificationHelper.sendChoreRejectedNotification(chore.assignedTo, chore, reason)
                    
                    _choreState.value = ChoreState.Success("Chore rejected")
                } else {
                    _choreState.value = ChoreState.Error("Chore not found")
                }
            } catch (e: Exception) {
                _choreState.value = ChoreState.Error(e.message ?: "Failed to reject chore")
            }
        }
    }
    
    fun deleteChore(choreId: String) {
        viewModelScope.launch {
            try {
                val chore = choreRepository.getChoreByIdSync(choreId)
                chore?.let {
                    choreRepository.deleteChore(it)
                    _choreState.value = ChoreState.Success("Chore deleted")
                }
            } catch (e: Exception) {
                _choreState.value = ChoreState.Error(e.message ?: "Failed to delete chore")
            }
        }
    }
    
    fun resetState() {
        _choreState.value = ChoreState.Idle
    }
}

sealed class ChoreState {
    object Idle : ChoreState()
    object Loading : ChoreState()
    data class Success(val message: String) : ChoreState()
    data class Error(val message: String) : ChoreState()
}

