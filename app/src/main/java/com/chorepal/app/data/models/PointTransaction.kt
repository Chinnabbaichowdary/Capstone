package com.chorepal.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "point_transactions")
data class PointTransaction(
    @PrimaryKey val transactionId: String,
    val userId: String, // Child userId
    val choreId: String?,
    val pointsChange: Int, // Positive for earning, negative for spending
    val transactionType: TransactionType,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis(),
    val performedBy: String // Parent or child userId
)

enum class TransactionType {
    EARNED,
    REDEEMED,
    BONUS,
    PENALTY,
    ADJUSTMENT
}

