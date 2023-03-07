package com.example.signup.ui.Home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.signup.R
import com.example.signup.ui.Home.home.HomeScreen
import com.example.signup.ui.Login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : ComponentActivity() {
    // 로그아웃 구현을 위한 변수
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient:GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        setContent {
            HomeScreen(logout = { logout() })

        }
    }

    // 로그아웃
    private fun logout() {
        auth.signOut()
        googleSignInClient?.signOut()
        saveData(loginEmail = null)
        finish()
    }

    // sharedpreference
    private fun saveData(loginEmail: String?) {
        val pref = getSharedPreferences("userEmail", MODE_PRIVATE) // shared key 설정
        val edit = pref.edit() // 수정 모드
        edit.putString("email", loginEmail) // 값 넣기
        edit.apply() // 적용하기
    }
}