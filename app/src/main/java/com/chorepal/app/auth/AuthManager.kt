package com.chorepal.app.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chorepal.app.data.models.User
import com.chorepal.app.data.models.UserType
import com.chorepal.app.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthManager(
    private val context: Context,
    private val userRepository: UserRepository
) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    
    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_TYPE_KEY = stringPreferencesKey("user_type")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }
    
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY] != null
    }
    
    val currentUserId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }
    
    val currentUserType: Flow<UserType?> = context.dataStore.data.map { preferences ->
        preferences[USER_TYPE_KEY]?.let { UserType.valueOf(it) }
    }
    
    suspend fun getCurrentUserIdSync(): String? {
        return context.dataStore.data.first()[USER_ID_KEY]
    }
    
    suspend fun getCurrentUserTypeSync(): UserType? {
        return context.dataStore.data.first()[USER_TYPE_KEY]?.let { UserType.valueOf(it) }
    }
    
    suspend fun getCurrentUserFromSession(): User? {
        val userId = getCurrentUserIdSync() ?: return null
        return userRepository.getUserByIdSync(userId)
    }
    
    suspend fun signUp(email: String, password: String, name: String, userType: UserType, familyCode: String? = null): Result<User> {
        return try {
            // Basic validation
            if (email.isBlank()) {
                throw Exception("Please enter an email address")
            }
            if (password.length < 6) {
                throw Exception("Password must be at least 6 characters")
            }
            
            // Let Firebase handle email format validation
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email.trim(), password).await()
            val firebaseUser = authResult.user ?: throw Exception("Failed to create user")
            
            // Send email verification
            firebaseUser.sendEmailVerification().await()
            
            // Update display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            firebaseUser.updateProfile(profileUpdates).await()
            
            // Determine parentId based on userType and familyCode
            val parentId: String? = if (userType == UserType.CHILD && familyCode != null) {
                val parent = userRepository.getParentByFamilyCode(familyCode)
                parent?.userId ?: throw Exception("Invalid family code. Please check with your parent.")
            } else null
            
            // Generate family code for parents
            val generatedFamilyCode: String? = if (userType == UserType.PARENT) {
                generateFamilyCode()
            } else null
            
            // Create user in local database (store email in lowercase for consistent lookups)
            val user = User(
                userId = firebaseUser.uid,
                email = email.trim().lowercase(),
                name = name,
                userType = userType,
                parentId = parentId,
                familyCode = generatedFamilyCode,
                totalPoints = 0
            )
            
            userRepository.insertUser(user)
            saveUserSession(user)
            
            Result.success(user)
        } catch (e: com.google.firebase.auth.FirebaseAuthException) {
            // Handle specific Firebase errors
            val errorMessage = when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "The email address is badly formatted"
                "ERROR_EMAIL_ALREADY_IN_USE" -> "This email is already registered"
                "ERROR_WEAK_PASSWORD" -> "Password must be at least 6 characters"
                "ERROR_OPERATION_NOT_ALLOWED" -> "Email/Password sign up is not enabled. Please contact support."
                else -> "Authentication error: ${e.localizedMessage}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Sign up failed: ${e.localizedMessage ?: e.message}"))
        }
    }
    
    private fun generateFamilyCode(): String {
        // Generate a 6-character family code (letters and numbers)
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789" // Excluding similar looking characters
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }
    
    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            if (email.isBlank()) {
                throw Exception("Please enter an email address")
            }
            
            // Let Firebase handle email validation
            val authResult = firebaseAuth.signInWithEmailAndPassword(email.trim(), password).await()
            val firebaseUser = authResult.user ?: throw Exception("Failed to sign in")
            
            // Get user from local database (use trimmed and lowercased email for consistent lookup)
            var user = userRepository.getUserByEmail(email.trim().lowercase())
            
            if (user == null) {
                // If user doesn't exist locally, create them (store email in lowercase)
                user = User(
                    userId = firebaseUser.uid,
                    email = email.trim().lowercase(),
                    name = firebaseUser.displayName ?: "User",
                    userType = UserType.PARENT, // Default to parent
                    familyCode = generateFamilyCode(), // Generate code for new parents
                    totalPoints = 0
                )
                userRepository.insertUser(user)
            } else {
                // Update existing user with family code if they don't have one
                if (user.userType == UserType.PARENT && user.familyCode == null) {
                    val updatedUser = user.copy(familyCode = generateFamilyCode())
                    userRepository.updateUser(updatedUser)
                    user = updatedUser
                }
            }
            
            saveUserSession(user)
            Result.success(user)
        } catch (e: com.google.firebase.auth.FirebaseAuthException) {
            // Handle specific Firebase errors
            val errorMessage = when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "The email address is badly formatted"
                "ERROR_WRONG_PASSWORD" -> "Incorrect password"
                "ERROR_USER_NOT_FOUND" -> "No account found with this email"
                "ERROR_USER_DISABLED" -> "This account has been disabled"
                "ERROR_OPERATION_NOT_ALLOWED" -> "Email/Password sign in is not enabled. Please contact support."
                else -> "Sign in error: ${e.localizedMessage}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Sign in failed: ${e.localizedMessage ?: e.message}"))
        }
    }
    
    suspend fun signOut() {
        firebaseAuth.signOut()
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    private suspend fun saveUserSession(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.userId
            preferences[USER_TYPE_KEY] = user.userType.name
            preferences[USER_EMAIL_KEY] = user.email
        }
    }
    
    fun getCurrentFirebaseUser(): FirebaseUser? = firebaseAuth.currentUser
    
    suspend fun sendEmailVerification(): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser
            if (user != null) {
                user.sendEmailVerification().await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("No user logged in"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun isEmailVerified(): Boolean {
        firebaseAuth.currentUser?.reload()?.await()
        return firebaseAuth.currentUser?.isEmailVerified ?: false
    }
}

