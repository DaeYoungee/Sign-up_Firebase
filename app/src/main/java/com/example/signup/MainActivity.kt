package com.example.signup

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.signup.ui.navigate.main.MainScreen
import com.example.signup.ui.navigate.main.MainViewModel
import com.example.signup.util.Auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by viewModels<MainViewModel>()

        super.onCreate(savedInstanceState)
        Log.d("Activity", "Main create")
        setContent {
            MainScreen(viewModel = viewModel)

        }
    }


}
