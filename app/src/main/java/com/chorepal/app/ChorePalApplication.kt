package com.chorepal.app

import android.app.Application
import com.google.firebase.FirebaseApp

class ChorePalApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }
}

