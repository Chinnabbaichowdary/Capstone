package com.chorepal.app.data.repository

import com.chorepal.app.data.dao.ChoreDao
import com.chorepal.app.data.models.Chore
import com.chorepal.app.data.models.ChoreStatus
import com.chorepal.app.data.models.ChoreType
import kotlinx.coroutines.flow.Flow

class ChoreRepository(private val choreDao: ChoreDao) {
    
    fun getChoreById(choreId: String): Flow<Chore?> = choreDao.getChoreById(choreId)
    
    suspend fun getChoreByIdSync(choreId: String): Chore? = choreDao.getChoreByIdSync(choreId)
    
    fun getChoresByChildId(childId: String): Flow<List<Chore>> = 
        choreDao.getChoresByChildId(childId)
    
    fun getChoresByParentId(parentId: String): Flow<List<Chore>> = 
        choreDao.getChoresByParentId(parentId)
    
    fun getChoresByChildIdAndStatus(childId: String, status: ChoreStatus): Flow<List<Chore>> = 
        choreDao.getChoresByChildIdAndStatus(childId, status)
    
    fun getChoresByParentIdAndStatus(parentId: String, status: ChoreStatus): Flow<List<Chore>> = 
        choreDao.getChoresByParentIdAndStatus(parentId, status)
    
    fun getChoresByChildIdAndType(childId: String, type: ChoreType): Flow<List<Chore>> = 
        choreDao.getChoresByChildIdAndType(childId, type)
    
    fun getPendingApprovalChores(parentId: String): Flow<List<Chore>> = 
        choreDao.getPendingApprovalChores(parentId)
    
    suspend fun insertChore(chore: Chore) = choreDao.insertChore(chore)
    
    suspend fun updateChore(chore: Chore) = choreDao.updateChore(chore)
    
    suspend fun deleteChore(chore: Chore) = choreDao.deleteChore(chore)
    
    suspend fun updateChoreStatus(choreId: String, status: ChoreStatus) = 
        choreDao.updateChoreStatus(choreId, status)
    
    suspend fun updateChoreStatusWithTimestamp(choreId: String, status: ChoreStatus, completedAt: Long) = 
        choreDao.updateChoreStatusWithTimestamp(choreId, status, completedAt)
}

