package com.example.signup.ui.navigate.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.signup.ui.navigate.logout.LogoutScreen
import com.example.signup.ui.navigate.signup.LoginScreen
import com.example.signup.ui.navigate.signup.SignUpScreen
import com.example.signup.util.Auth

class Route {
    companion object {
        const val LOGIN_SCREEN = "login"
        const val LOGOUT_SCREEN = "logout"
        const val SIGNUP_SCREEN = "signup"
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        Surface(modifier = Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = Route.LOGIN_SCREEN) {
                composable(route = Route.LOGIN_SCREEN) {
                    LoginScreen(
                        viewModel = viewModel,
                        scaffoldState = scaffoldState,
                        onMoveLogout = { navController.navigate(Route.LOGOUT_SCREEN) },
                        onMoveSignup = { navController.navigate((Route.SIGNUP_SCREEN)) },
                    )
                }
                composable(route = Route.LOGOUT_SCREEN) {
                    LogoutScreen(viewModel = viewModel, onMoveLogin = {navController.navigate(Route.LOGIN_SCREEN)})
                }
                composable(route = Route.SIGNUP_SCREEN) {
                    SignUpScreen(
                        viewModel = viewModel,
                        scaffoldState = scaffoldState,
                        onMoveLogout = { navController.navigate(Route.LOGOUT_SCREEN) },
                    )
                }
            }
        }
    }
}
