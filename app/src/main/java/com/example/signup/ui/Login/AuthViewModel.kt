package com.example.signup.ui.Login

import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
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
    fun signup(email: String?, password: String?, scaffoldState: ScaffoldState, onMoveLogout: () -> Unit) {

        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            viewModelScope.launch { scaffoldState.snackbarHostState.showSnackbar(message = "이메일 비밀번호를 입력하세요") }
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { sendTask ->
                                if (sendTask.isSuccessful) {
                                    Log.d("LoginTest", "회원가입 성공 ${auth.currentUser?.email}")
                                    onMoveLogout()
                                } else { Log.d("LoginTest", "메일 전송 fail") }
                            }
                    } else {
                        Log.d("LoginTest", "회원가입 fail ${auth.currentUser}, 에러 ${task.exception}")
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
    ) {
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            viewModelScope.launch { scaffoldState.snackbarHostState.showSnackbar(message = "로그인 실패") }
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LoginTest", "로그인 성공, 현재 사용자 ${auth.currentUser?.email}")
                    if (checkAuth()) {
                        // 로그인 성공
                        this.email = email
                        Log.d("LoginTest", "login success")
                        onMoveLogout()
                    } else {
                        // 발송된 메일로 인증 확인을 안 한 경우
                        viewModelScope.launch { scaffoldState.snackbarHostState.showSnackbar(message = "메일 인증을 하지 않았습니다.") }
                        Log.d("LoginTest", "인증 fail")
                    }
                } else {
                    viewModelScope.launch { scaffoldState.snackbarHostState.showSnackbar(message = "로그인 실패") }
                    Log.d("LoginTest", "로그인 fail")
                }
            }
        }
    }

    // 로그아웃
    fun logout(onMoveLogin:() -> Unit) {
        auth.signOut()
        this.email = null
        Log.d("LoginTest", "로그아웃, 현재 사용자 ${auth.currentUser?.email}")
        onMoveLogin()
    }

    // 구글 로그인
    fun googleLogin() {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(resources.getString(
//            R.string.default_web_client_id))
    }
}