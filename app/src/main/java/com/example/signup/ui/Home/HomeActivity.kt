package com.example.signup.ui.Home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.signup.R
import com.example.signup.data.Post
import com.example.signup.data.User
import com.example.signup.ui.Home.navigation.MainScreenView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    // 로그아웃 구현을 위한 변수
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var documentId = 1;
    private val currentUser: FirebaseUser? = auth.currentUser
    private var isExecuted = false
    val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        setContent {

            MainScreenView(
                logout = { logout() },
                documentCount = { documentCount() },
                viewModel = viewModel,
                getPost = { getPost() }
            )
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
    private val saveUserStore: (String) -> Unit = { name ->
        userUpdate(name = name)
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
                    db.collection(
                        "users"
                    ).document("user$documentId").set(data)
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

    // 게시글 업로드
    private val savePostStore: (Int) -> Unit = { count ->
        val data = viewModel.post.value.copy()
        viewModel.setPost(post = Post(title = "", category = application.getString(R.string.text_category_choice), content= ""))

        db.collection("post").document("post${count}").set(data).addOnSuccessListener {
            Toast.makeText(applicationContext, "데이터가 저정되었습니다.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Log.d("TAG", " 왜 안될까 fail")
        }


    }

    // 현재 파이어스토어 도큐먼트 개수 확인 및 저장
    private fun documentCount() {
        db.collection("post").get().addOnSuccessListener { querySnapshot ->
            Log.d("EXCEPTION", "현재 도큐먼트 개수: ${querySnapshot.size()}")
            savePostStore(querySnapshot.size())
            viewModel.setDocumentCount(querySnapshot.size())
        }.addOnFailureListener { exception ->
            Log.d("EXCEPTION", "도큐먼트 개수 실패${exception.message}")
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

    private fun getPost() {
        viewModel.setInitList()
        db.collection("post").get().addOnSuccessListener {result ->
            for (document in result) {
                viewModel.setAddList(document.toObject(Post::class.java))
                Log.d("MYTEST","${document.toObject(Post::class.java)}")
            }
            viewModel.setScreenState()
        }.addOnFailureListener {
            Log.d("EXCEPTION", "${it.message}")
        }
    }

}
