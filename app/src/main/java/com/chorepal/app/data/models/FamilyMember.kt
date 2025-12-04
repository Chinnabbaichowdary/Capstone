package com.chorepal.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "family_members")
data class FamilyMember(
    @PrimaryKey val memberId: String,
    val familyId: String,
    val userId: String,
    val role: UserType,
    val joinedAt: Long = System.currentTimeMillis()
)

