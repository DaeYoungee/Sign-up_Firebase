package com.example.signup.ui.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.signup.R
import com.example.signup.ui.Home.HomeActivity
import com.example.signup.ui.Login.navigate.main.MainScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val account = GoogleSignIn.getLastSignedInAccount(this)?.let {
                startActivity(Intent(this, HomeActivity::class.java))
            } ?: if (authEmail() != null) {
                Log.d("TestLogin", "1 + ${authEmail()}")
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                Log.d("TestLogin", "${authEmail()}")
            }
            MainScreen(
                googleLogin = { googleLogin() },
                signup = signup,
                login = login,
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
    val signup: (String?, String?) -> Unit = { email, password ->
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
    val login: (String?, String?) -> Unit = { email, password ->
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (checkAuth()) {
                        // 로그인 성공
                        this.email = email
                        saveData(email)
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

    // 홈 스크린 이동
    private fun onMoveHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    // sharedpreference
    private fun saveData(loginEmail: String?) {
        val pref = getSharedPreferences("userEmail", MODE_PRIVATE) // shared key 설정
        val edit = pref.edit() // 수정 모드
        edit.putString("email", loginEmail) // 값 넣기
        edit.apply() // 적용하기
    }

    private fun authEmail(): String? {
        val pref = getSharedPreferences("userEmail", MODE_PRIVATE)
        return pref.getString("email", null)
    }


}
