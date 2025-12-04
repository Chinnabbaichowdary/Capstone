package com.chorepal.app.data.dao

import androidx.room.*
import com.chorepal.app.data.models.Chore
import com.chorepal.app.data.models.ChoreStatus
import com.chorepal.app.data.models.ChoreType
import kotlinx.coroutines.flow.Flow

@Dao
interface ChoreDao {
    @Query("SELECT * FROM chores WHERE choreId = :choreId")
    fun getChoreById(choreId: String): Flow<Chore?>
    
    @Query("SELECT * FROM chores WHERE choreId = :choreId")
    suspend fun getChoreByIdSync(choreId: String): Chore?
    
    @Query("SELECT * FROM chores WHERE assignedTo = :childId ORDER BY createdAt DESC")
    fun getChoresByChildId(childId: String): Flow<List<Chore>>
    
    @Query("SELECT * FROM chores WHERE createdBy = :parentId ORDER BY createdAt DESC")
    fun getChoresByParentId(parentId: String): Flow<List<Chore>>
    
    @Query("SELECT * FROM chores WHERE assignedTo = :childId AND status = :status ORDER BY createdAt DESC")
    fun getChoresByChildIdAndStatus(childId: String, status: ChoreStatus): Flow<List<Chore>>
    
    @Query("SELECT * FROM chores WHERE createdBy = :parentId AND status = :status ORDER BY createdAt DESC")
    fun getChoresByParentIdAndStatus(parentId: String, status: ChoreStatus): Flow<List<Chore>>
    
    @Query("SELECT * FROM chores WHERE assignedTo = :childId AND choreType = :type ORDER BY createdAt DESC")
    fun getChoresByChildIdAndType(childId: String, type: ChoreType): Flow<List<Chore>>
    
    @Query("SELECT * FROM chores WHERE status = 'COMPLETED_PENDING_APPROVAL' AND createdBy = :parentId ORDER BY completedAt DESC")
    fun getPendingApprovalChores(parentId: String): Flow<List<Chore>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChore(chore: Chore)
    
    @Update
    suspend fun updateChore(chore: Chore)
    
    @Delete
    suspend fun deleteChore(chore: Chore)
    
    @Query("UPDATE chores SET status = :status WHERE choreId = :choreId")
    suspend fun updateChoreStatus(choreId: String, status: ChoreStatus)
    
    @Query("UPDATE chores SET status = :status, completedAt = :completedAt WHERE choreId = :choreId")
    suspend fun updateChoreStatusWithTimestamp(choreId: String, status: ChoreStatus, completedAt: Long)
}

