package com.example.signup.ui.Home.navigation.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.signup.ui.Home.HomeViewModel

@Composable
fun TimelineScreen(viewModel: HomeViewModel, getPost: () -> Unit) {


    LaunchedEffect(viewModel.screenState.value) {
        getPost()
    }

    if (!viewModel.screenState.value) {
        CustomCircularProgressBar()
    } else {
        LazyColumn() {
            item { Text(text = "시작") }
            items(viewModel.list.value) {item ->
                Button(onClick = { /*TODO*/ }) {
                    Row() {
                        Text(text = item.title)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomCircularProgressBar() {
    CircularProgressIndicator(
        modifier = Modifier.size(100.dp),
        color = Color.Green,
        strokeWidth = 10.dp
    )
    Text(text = "안녕")
}