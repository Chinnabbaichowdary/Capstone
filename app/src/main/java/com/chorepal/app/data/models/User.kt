package com.chorepal.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String,
    val email: String,
    val name: String,
    val userType: UserType,
    val parentId: String? = null, // For children, links to parent
    val familyCode: String? = null, // Unique code for parents to share with children
    val profileImageUrl: String? = null,
    val totalPoints: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val fcmToken: String? = null
)

enum class UserType {
    PARENT,
    CHILD
}

