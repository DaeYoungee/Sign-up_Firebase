package com.example.signup.ui.Home.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(logout: () -> Unit) {
    Column() {
        Text(text = "로그인 축하드립니다.")
        Button(onClick = { logout() }) {
            Text(text = "로그아웃")
        }

    }

}