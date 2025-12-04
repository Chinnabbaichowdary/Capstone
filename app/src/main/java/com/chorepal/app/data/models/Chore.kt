package com.chorepal.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chores")
data class Chore(
    @PrimaryKey val choreId: String,
    val title: String,
    val description: String,
    val pointsValue: Int,
    val choreType: ChoreType,
    val createdBy: String, // Parent userId
    val assignedTo: String?, // Child userId
    val status: ChoreStatus,
    val dueDate: Long? = null,
    val completedAt: Long? = null,
    val verifiedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isRecurring: Boolean = false,
    val recurringFrequency: RecurringFrequency? = null,
    val imageProofUrl: String? = null,
    val notes: String? = null
)

enum class ChoreType {
    DAILY,
    BONUS,
    WEEKLY
}

enum class ChoreStatus {
    ASSIGNED,
    IN_PROGRESS,
    COMPLETED_PENDING_APPROVAL,
    APPROVED,
    REJECTED
}

enum class RecurringFrequency {
    DAILY,
    WEEKLY,
    BIWEEKLY,
    MONTHLY
}

