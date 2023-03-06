package com.example.signup.ui.navigate.logout

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.signup.ui.navigate.main.MainViewModel

@Composable
fun LogoutScreen(viewModel: MainViewModel, onMoveLogin: () -> Unit) {
    Column() {
        Text(text = "로그인 축하드립니다.")
        Button(onClick = { viewModel.logout(onMoveLogin = onMoveLogin) }) {
            Text(text = "로그아웃")
        }
    }

}