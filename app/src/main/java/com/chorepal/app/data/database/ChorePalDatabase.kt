package com.chorepal.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chorepal.app.data.dao.*
import com.chorepal.app.data.models.*

@Database(
    entities = [
        User::class,
        Chore::class,
        PointTransaction::class,
        AppNotification::class,
        FamilyMember::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ChorePalDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun choreDao(): ChoreDao
    abstract fun pointTransactionDao(): PointTransactionDao
    abstract fun notificationDao(): NotificationDao
    
    companion object {
        @Volatile
        private var INSTANCE: ChorePalDatabase? = null
        
        fun getDatabase(context: Context): ChorePalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChorePalDatabase::class.java,
                    "chorepal_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

