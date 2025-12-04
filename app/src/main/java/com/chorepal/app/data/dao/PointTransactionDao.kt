package com.chorepal.app.data.dao

import androidx.room.*
import com.chorepal.app.data.models.PointTransaction
import com.chorepal.app.data.models.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface PointTransactionDao {
    @Query("SELECT * FROM point_transactions WHERE userId = :userId ORDER BY timestamp DESC")
    fun getTransactionsByUserId(userId: String): Flow<List<PointTransaction>>
    
    @Query("SELECT * FROM point_transactions WHERE userId = :userId AND transactionType = :type ORDER BY timestamp DESC")
    fun getTransactionsByUserIdAndType(userId: String, type: TransactionType): Flow<List<PointTransaction>>
    
    @Query("SELECT * FROM point_transactions WHERE choreId = :choreId")
    suspend fun getTransactionByChoreId(choreId: String): PointTransaction?
    
    @Query("SELECT SUM(pointsChange) FROM point_transactions WHERE userId = :userId")
    suspend fun getTotalPointsByUserId(userId: String): Int?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: PointTransaction)
    
    @Delete
    suspend fun deleteTransaction(transaction: PointTransaction)
    
    @Query("SELECT * FROM point_transactions WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentTransactions(userId: String, limit: Int): Flow<List<PointTransaction>>
}

