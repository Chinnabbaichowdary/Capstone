package com.chorepal.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.chorepal.app.ui.screens.auth.EmailVerificationScreen
import com.chorepal.app.ui.screens.auth.LoginScreen
import com.chorepal.app.ui.screens.auth.SignUpScreen
import com.chorepal.app.ui.screens.child.ChildDashboardScreen
import com.chorepal.app.ui.screens.parent.ChildDetailsScreen
import com.chorepal.app.ui.screens.parent.ParentDashboardScreen
import com.chorepal.app.viewmodel.AuthViewModel
import kotlinx.coroutines.runBlocking

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object EmailVerification : Screen("email_verification")
    object ParentDashboard : Screen("parent_dashboard")
    object ChildDashboard : Screen("child_dashboard")
    object ChildDetails : Screen("child_details/{childId}") {
        fun createRoute(childId: String) = "child_details/$childId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                onLoginSuccess = { userType ->
                    val destination = when (userType) {
                        com.chorepal.app.data.models.UserType.PARENT -> Screen.ParentDashboard.route
                        com.chorepal.app.data.models.UserType.CHILD -> Screen.ChildDashboard.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }
        
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateBack = { navController.popBackStack() },
                onSignUpSuccess = { userType ->
                    // Navigate to email verification screen
                    navController.navigate(Screen.EmailVerification.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }
        
        composable(Screen.EmailVerification.route) {
            val currentUser by authViewModel.currentUser.collectAsState()
            
            EmailVerificationScreen(
                email = currentUser?.email ?: "",
                userType = currentUser?.userType ?: com.chorepal.app.data.models.UserType.PARENT,
                onEmailVerified = { userType ->
                    val destination = when (userType) {
                        com.chorepal.app.data.models.UserType.PARENT -> Screen.ParentDashboard.route
                        com.chorepal.app.data.models.UserType.CHILD -> Screen.ChildDashboard.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onResendEmail = {
                    runBlocking {
                        authViewModel.resendVerificationEmail()
                    }
                },
                onCheckVerification = {
                    authViewModel.checkEmailVerified()
                }
            )
        }
        
        composable(Screen.ParentDashboard.route) {
            ParentDashboardScreen(
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onChildClick = { childId ->
                    navController.navigate(Screen.ChildDetails.createRoute(childId))
                },
                authViewModel = authViewModel
            )
        }
        
        composable(
            route = Screen.ChildDetails.route,
            arguments = listOf(navArgument("childId") { type = NavType.StringType })
        ) { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId") ?: ""
            ChildDetailsScreen(
                childId = childId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.ChildDashboard.route) {
            ChildDashboardScreen(
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }
    }
}

