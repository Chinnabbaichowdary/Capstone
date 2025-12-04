package com.chorepal.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chorepal.app.auth.AuthManager
import com.chorepal.app.data.models.User
import com.chorepal.app.data.models.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authManager: AuthManager) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    init {
        checkAuthStatus()
    }
    
    private fun checkAuthStatus() {
        viewModelScope.launch {
            authManager.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    val userId = authManager.getCurrentUserIdSync()
                    // Load actual user data from database for session restoration
                    val user = authManager.getCurrentUserFromSession()
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated(userId ?: "")
                } else {
                    _authState.value = AuthState.Unauthenticated
                    _currentUser.value = null
                }
            }
        }
    }
    
    fun signUp(email: String, password: String, name: String, userType: UserType, familyCode: String? = null) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authManager.signUp(email, password, name, userType, familyCode)
            result.fold(
                onSuccess = { user ->
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated(user.userId)
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error(error.message ?: "Sign up failed")
                }
            )
        }
    }
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // Clear any previous user data
            _currentUser.value = null
            
            val result = authManager.signIn(email, password)
            result.fold(
                onSuccess = { user ->
                    // Ensure user type is correctly set
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated(user.userId)
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error(error.message ?: "Sign in failed")
                }
            )
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            authManager.signOut()
            _currentUser.value = null
            _authState.value = AuthState.Unauthenticated
        }
    }
    
    fun resendVerificationEmail() {
        viewModelScope.launch {
            authManager.sendEmailVerification()
        }
    }
    
    suspend fun checkEmailVerified(): Boolean {
        return authManager.isEmailVerified()
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Authenticated(val userId: String) : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

