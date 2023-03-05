package com.example.signup.ui.navigate.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.signup.ui.navigate.logout.LogoutScreen
import com.example.signup.ui.navigate.signup.LoginScreen
import com.example.signup.ui.navigate.signup.SignUpScreen

class Route {
    companion object {
        const val LOGIN_SCREEN = "login"
        const val LOGOUT_SCREEN = "logout"
        const val SIGNUP_SCREEN = "signup"
    }
}

@Composable
fun MyNavigator(viewModel: MainViewModel) {
    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = Route.LOGIN_SCREEN) {
        composable(route = Route.LOGIN_SCREEN) {
            LoginScreen(auth = viewModel.auth)
        }
        composable(route = Route.LOGOUT_SCREEN) {
            SignUpScreen()
        }
        composable(route = Route.SIGNUP_SCREEN) {
            LogoutScreen()
        }
    }
}