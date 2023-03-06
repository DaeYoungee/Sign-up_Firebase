package com.example.signup.ui.Auth.navigate.main

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
import com.example.signup.ui.Auth.navigate.signup.SignUpScreen
import com.example.signup.ui.navigate.signup.LoginScreen

class Route {
    companion object {
        const val LOGIN_SCREEN = "login"
        const val SIGNUP_SCREEN = "signup"
    }
}

@Composable
fun MainScreen(
    googleLogin: () -> Unit,
    signup: (String?, String?) -> Unit,
    login: (String?, String?) -> Unit,
    logout: () -> Unit
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        Surface(modifier = Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = Route.LOGIN_SCREEN) {
                composable(route = Route.LOGIN_SCREEN) {
                    LoginScreen(
                        onMoveSignup = { navController.navigate((Route.SIGNUP_SCREEN)) },
                        login = login,
                        onGoogleLogin = googleLogin
                    )
                }
                composable(route = Route.SIGNUP_SCREEN) {
                    SignUpScreen( signup = signup )
                }
            }
        }
    }
}
