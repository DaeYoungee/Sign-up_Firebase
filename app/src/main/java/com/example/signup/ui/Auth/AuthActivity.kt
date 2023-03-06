package com.example.signup.ui.Auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.signup.R
import com.example.signup.ui.Auth.navigate.main.MainScreen
import com.example.signup.ui.Home.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by viewModels<AuthViewModel>()
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(
                googleLogin = { googleLogin() },
                signup = signup,
                login = login,
                logout = { logout() },
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    var auth: FirebaseAuth = FirebaseAuth.getInstance() /* 파이어베어스 인증 객체 */
    var email: String? = null
    private val requestLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.d("AuthActivityTest", "success")
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        this.email = account.email
                        onMoveHome()
                    } else {
                        Toast.makeText(this, "구글 로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "구글 회원가입에 실패하였습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    // 구글 로그인
    private fun googleLogin() {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // 구글의 인증 관리 앱 실행
        val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
        requestLauncher.launch(signInIntent)
    }


    // 사용자의 이메일 정보, 인증 상태 파악
    private fun checkAuth(): Boolean {
        val currentUser = auth.currentUser
        return currentUser?.let {
            email = currentUser.email
            currentUser.isEmailVerified
        } ?: let { false }
    }


    // 이메일, 비밀번호 회원가입
    val signup:(String?, String?) -> Unit = {email, password ->
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { sendTask ->
                                if (sendTask.isSuccessful) {
                                    onMoveHome()
                                } else {
                                    Log.d("LoginTest", "메일 전송 fail")
                                }
                            }
                    } else {
                        Log.d("LoginTest", "회원가입 fail")
                    }
                }
        }
    }

    // 이메일, 비밀번호 로그인
    val login:(String?, String?) -> Unit = {email, password ->
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LoginTest", "show text")
                    if (checkAuth()) {
                        // 로그인 성공
                        this.email = email
                        Log.d("LoginTest", "login success")
                        onMoveHome()
                    } else {
                        // 발송된 메일로 인증 확인을 안 한 경우

                        Log.d("LoginTest", "인증 fail")
                    }
                } else {
                    Log.d("LoginTest", "로그인 fail")
                    Toast.makeText(this, "이메일과 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    // 로그아웃
    fun logout() {
        auth.signOut()
        this.email = null
        startActivity(Intent(this, AuthActivity::class.java))
    }
    // 홈 스크린 이동
    fun onMoveHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

}