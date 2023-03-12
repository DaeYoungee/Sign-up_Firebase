package com.example.signup.ui.Home.navigation.screen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.signup.R
import com.example.signup.data.Post
import com.example.signup.ui.Home.HomeViewModel
import com.example.signup.widget.Dialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(savePostStore: suspend () -> Unit, viewModel: HomeViewModel) {
    var dialogState by rememberSaveable {
        mutableStateOf(false)
    }
    val categotyChoice = stringResource(id = R.string.text_category_choice)

    val scope = rememberCoroutineScope()

    if (dialogState) {
        Dialog(onClick = {dialogState = false}, viewModel = viewModel)
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Box(modifier = Modifier) {
            Canvas(
                modifier = Modifier
                    .padding(16.dp)
                    .size(60.dp)
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))

            ) {}
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                contentDescription = "camera"
            )
        }
        Divider(thickness = 1.dp)

        BasicTextField(
            value = viewModel.post.value.title,
            onValueChange = { viewModel.setTitle(it) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    innerTextField()
                    if (viewModel.post.value.title == "") {
                        Text(
                            text = stringResource(id = R.string.text_title),
                            color = Color.Gray,
                        )
                    }
                }
            },
        )

        Divider(thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(), onClick = { dialogState = true }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = viewModel.post.value.category)
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = "show dialog"
                )
            }
        }

        BasicTextField(
            value = viewModel.post.value.content,
            onValueChange = { viewModel.setContent(it) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp),
            maxLines = Int.MAX_VALUE,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//            keyboardActions = KeyboardActions(onNext = {}),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    innerTextField()
                    if (viewModel.post.value.content == "") {
                        Text(
                            text = stringResource(id = R.string.text_content),
                            color = Color.Gray,
                        )
                    }
                }
            },
        )

        Button(
            onClick = {
                scope.launch {
                    savePostStore()
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End)
        ) {
            Text(text = "데이터 저장")
        }
    }


}