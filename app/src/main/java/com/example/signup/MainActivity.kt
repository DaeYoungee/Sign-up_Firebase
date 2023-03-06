package com.example.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.signup.ui.Auth.AuthActivity
import com.example.signup.ui.Home.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn

class MainActivity : ComponentActivity() {

    override fun onStart() {
        super.onStart()
        Log.d("SIGN UP Log", "MainActiviy onStart")
        val account = GoogleSignIn.getLastSignedInAccount(this)?.let {
            startActivity(Intent(this, HomeActivity::class.java))
        } ?: startActivity(Intent(this, AuthActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SIGN UP", "MainActiviy onCreate")
        setContent {

        }
    }


}
