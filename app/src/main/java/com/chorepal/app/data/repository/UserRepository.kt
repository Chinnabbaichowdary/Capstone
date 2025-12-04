package com.chorepal.app.data.repository

import com.chorepal.app.data.dao.UserDao
import com.chorepal.app.data.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository(private val userDao: UserDao) {
    
    fun getUserById(userId: String): Flow<User?> = userDao.getUserById(userId)
    
    suspend fun getUserByIdSync(userId: String): User? = userDao.getUserByIdSync(userId)
    
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    
    fun getChildrenByParentId(parentId: String): Flow<List<User>> = 
        userDao.getChildrenByParentId(parentId)
    
    suspend fun getChildrenByParentIdSync(parentId: String): List<User> = 
        userDao.getChildrenByParentIdSync(parentId)
    
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    
    suspend fun updateUserPoints(userId: String, points: Int) = 
        userDao.updateUserPoints(userId, points)
    
    suspend fun updateFcmToken(userId: String, token: String) = 
        userDao.updateFcmToken(userId, token)
    
    suspend fun getParentByFamilyCode(familyCode: String): User? = 
        userDao.getParentByFamilyCode(familyCode)
}

