package com.chorepal.app.data.repository

import com.chorepal.app.data.dao.PointTransactionDao
import com.chorepal.app.data.dao.UserDao
import com.chorepal.app.data.models.PointTransaction
import com.chorepal.app.data.models.TransactionType
import kotlinx.coroutines.flow.Flow

class PointRepository(
    private val pointTransactionDao: PointTransactionDao,
    private val userDao: UserDao
) {
    
    fun getTransactionsByUserId(userId: String): Flow<List<PointTransaction>> = 
        pointTransactionDao.getTransactionsByUserId(userId)
    
    fun getTransactionsByUserIdAndType(userId: String, type: TransactionType): Flow<List<PointTransaction>> = 
        pointTransactionDao.getTransactionsByUserIdAndType(userId, type)
    
    suspend fun getTransactionByChoreId(choreId: String): PointTransaction? = 
        pointTransactionDao.getTransactionByChoreId(choreId)
    
    suspend fun getTotalPointsByUserId(userId: String): Int = 
        pointTransactionDao.getTotalPointsByUserId(userId) ?: 0
    
    suspend fun insertTransaction(transaction: PointTransaction) {
        pointTransactionDao.insertTransaction(transaction)
        // Update user's total points
        val currentTotal = getTotalPointsByUserId(transaction.userId)
        userDao.updateUserPoints(transaction.userId, currentTotal)
    }
    
    suspend fun deleteTransaction(transaction: PointTransaction) = 
        pointTransactionDao.deleteTransaction(transaction)
    
    fun getRecentTransactions(userId: String, limit: Int): Flow<List<PointTransaction>> = 
        pointTransactionDao.getRecentTransactions(userId, limit)
}

