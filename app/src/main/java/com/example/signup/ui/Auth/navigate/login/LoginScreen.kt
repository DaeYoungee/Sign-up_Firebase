package com.example.signup.ui.navigate.signup

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signup.R
import com.example.signup.ui.Auth.AuthViewModel

@Composable
fun LoginScreen(
    onMoveSignup: () -> Unit,
    onGoogleLogin: () -> Unit,
    login: (String?, String?) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var email by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login...", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "email")},
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                Log.d("FocusTest", "success1")
                focusRequester.requestFocus()
            })
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.focusRequester(focusRequester = focusRequester),
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(text = "password - 6 characters or more") },
            label = { Text(text = "password")},
            maxLines = 1,
            /* imeAction이 바뀌지 않음 나중에 바꿔보자 */
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = {
                Log.d ("FocusTest", "success2")
                focusManager.clearFocus()
                login(email, password)
                email=""
                password=""
            })

        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { login(email, password) },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Gray,
                contentColor = Color.White
            )
        ) {
            Text(text = "login")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onMoveSignup() },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Gray,
                contentColor = Color.White
            )
        ) {
            Text(text = "sign up")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onGoogleLogin() },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Gray,
                contentColor = Color.White
            )
        ) {
            Icon(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = "google certification",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = "google certification")
        }
    }
}