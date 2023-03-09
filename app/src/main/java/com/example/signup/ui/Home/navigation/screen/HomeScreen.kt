package com.example.signup.ui.Home.navigation.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(logout: () -> Unit, saveStore: (String) -> Unit) {
    var name by rememberSaveable {
        mutableStateOf("")
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "로그인 축하드립니다.")

        Button(onClick = { logout() }) {
            Text(text = "로그아웃")
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = {name = it})
        Button(onClick = {
            Log.d("TAG", "State: $name")
            saveStore(name)
            name = ""
        }) {
            Text(text = "데이터 저장")
        }

    }

}