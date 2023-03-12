package com.example.signup.ui.Home

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.signup.R
import com.example.signup.data.Post
import kotlinx.coroutines.flow.flow
import java.util.Objects
import java.util.concurrent.Flow

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _post = mutableStateOf<Post>(
        Post(
            title = "",
            category = application.getString(R.string.text_category_choice),
            content = ""
        )
    )
    val post: State<Post> = _post

    private val _list = mutableStateOf(mutableListOf<Post>())
    val list: State<List<Post>> = _list

    private val _documentCount = mutableStateOf(0)
    val documentCount: State<Int> = _documentCount

    private val _screenState = mutableStateOf(false)
    val screenState:State<Boolean> = _screenState
    fun setTitle(title: String) {
        _post.value = _post.value.copy(title = title)
        Log.d("MYTEST", "${_post.value}")

    }

    fun setCategory(category: String) {
        _post.value = _post.value.copy(category = category)
        Log.d("MYTEST", "${_post.value}")

    }

    fun setContent(content: String) {
        _post.value = _post.value.copy(content = content)
        Log.d("MYTEST", "${_post.value}")

    }
    fun setPost(post: Post) {
        _post.value = post
    }

    fun setDocumentCount(count: Int) {
        _documentCount.value = count
    }

    fun setAddList(post: Post) {
        _list.value.add(post)
    }

    fun setInitList() {
        _list.value.clear()
    }

    fun setScreenState() {
        _screenState.value = true
    }
}