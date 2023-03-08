package com.example.signup.ui.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.signup.R
import com.example.signup.data.User
import com.example.signup.ui.Home.home.HomeScreen
import com.example.signup.ui.Login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class HomeActivity : ComponentActivity() {
    // 로그아웃 구현을 위한 변수
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var documentId = 1;
    private val currentUser: FirebaseUser? = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable(route = "home") {
                    HomeScreen(logout = { logout() }, saveStore = saveStore)

                }
            }

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
    // firebase store에 저장
    private val saveStore: (String) -> Unit = { name ->
        Log.d("TAG", "saveStore: $name")
        userUpdate(name = name)

        Log.d("TAG", "saveStore + current.user: ${currentUser?.displayName}")
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                if (currentUser?.displayName.toString() == name) {
                    val data = User(
                        name = currentUser?.displayName.toString(),
                        email = currentUser?.email.toString(),
                        age = 24,
                        isAdmin = false,
                        uid = currentUser?.uid.toString(),
                    )
                    db.collection("users").document("user$documentId").set(data)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext, "데이터가 저정되었습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }.addOnFailureListener {
                            Log.d("TAG", " 왜 안될까 fail")
                        }
                    documentId++
                    break
                }
            }


        }



    }

    // firebase 사용자 업데이트
    private fun userUpdate(name: String) {
        Log.d("TAG", "userUpdate: $name")

        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "userUpdate + current.user: ${currentUser.displayName}")
            }
        }

    }

    private fun uploadImage(docId: String) {}
}