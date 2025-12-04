package com.chorepal.app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chorepal.app.data.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserById(userId: String): Flow<User?>
    
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserByIdSync(userId: String): User?
    
    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email)")
    suspend fun getUserByEmail(email: String): User?
    
    @Query("SELECT * FROM users WHERE parentId = :parentId")
    fun getChildrenByParentId(parentId: String): Flow<List<User>>
    
    @Query("SELECT * FROM users WHERE parentId = :parentId")
    suspend fun getChildrenByParentIdSync(parentId: String): List<User>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    
    @Update
    suspend fun updateUser(user: User)
    
    @Delete
    suspend fun deleteUser(user: User)
    
    @Query("UPDATE users SET totalPoints = :points WHERE userId = :userId")
    suspend fun updateUserPoints(userId: String, points: Int)
    
    @Query("UPDATE users SET fcmToken = :token WHERE userId = :userId")
    suspend fun updateFcmToken(userId: String, token: String)
    
    @Query("SELECT * FROM users WHERE familyCode = :familyCode AND userType = 'PARENT'")
    suspend fun getParentByFamilyCode(familyCode: String): User?
}

