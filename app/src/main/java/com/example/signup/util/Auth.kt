package com.example.signup.util

import android.util.Log
import androidx.compose.material.ScaffoldState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking



class Auth {
    var auth: FirebaseAuth = FirebaseAuth.getInstance() /* 파이어베어스 인증 객체 */
    var email: String? = null

    // 사용자의 이메일 정보, 인증 상태 파악
    fun checkAuth(): Boolean {
        val currentUser = auth.currentUser
        return currentUser?.let {
            email = currentUser.email
            currentUser.isEmailVerified
        } ?: let { false }
    }


    // 이메일, 비밀번호 회원가입
    fun signup(email: String?, password: String?, scaffoldState: ScaffoldState, onMoveLogout: () -> Unit) = runBlocking{
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            launch { scaffoldState.snackbarHostState.showSnackbar(message = "값을 입력하세요") }
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { sendTask ->
                                if (sendTask.isSuccessful) { onMoveLogout() }
                                else { Log.d("LoginTest", "메일 전송 fail") }
                            }
                    } else {
                        Log.d("LoginTest", "회원가입 fail")
                    }
                }
        }
    }

    // 이메일, 비밀번호 로그인
    fun login(
        email: String?,
        password: String?,
        scaffoldState: ScaffoldState,
        onMoveLogout: () -> Unit
    ) = runBlocking {
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            launch { scaffoldState.snackbarHostState.showSnackbar(message = "로그인 실패") }
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LoginTest", "show text")
                    if (checkAuth()) {
                        // 로그인 성공
//                        this.email = email
                        Log.d("LoginTest", "login success")
                        onMoveLogout()
                    } else {
                        // 발송된 메일로 인증 확인을 안 한 경우

                        Log.d("LoginTest", "인증 fail")
                    }
                } else {
                    Log.d("LoginTest", "로그인 fail")
                }
            }
        }

    }

    // 로그아웃
    fun logout() {
        auth.signOut()
        this.email = null
    }

}