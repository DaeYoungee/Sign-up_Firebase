package com.example.signup.ui.Home.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomeScreen(logout: () -> Unit, saveStore: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "로그인 축하드립니다.")

        Button(onClick = { logout() }) {
            Text(text = "로그아웃")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { saveStore() }) {
            Text(text = "데이터 저장")
        }

    }

}