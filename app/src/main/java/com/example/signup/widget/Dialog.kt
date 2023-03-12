package com.example.signup.widget

import android.provider.Settings.System.getString
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.signup.R
import com.example.signup.ui.Home.HomeViewModel


@Composable
fun Dialog(onClick: () -> Unit, viewModel: HomeViewModel) {
    androidx.compose.ui.window.Dialog(onDismissRequest = { onClick() }) {
        Surface(
            modifier = Modifier
                .width(200.dp)
                .wrapContentHeight(), color = Color.White
        ) {
            DialogContent(onClick, viewModel = viewModel)
        }

    }
}


@Composable
fun DialogContent(onClick: () -> Unit, viewModel: HomeViewModel) {
    val context = LocalContext.current
    val list = listOf<String>(
        stringResource(id = R.string.text_digital),
        stringResource(id = R.string.text_home_product),
        stringResource(id = R.string.text_furniture),
        stringResource(id = R.string.text_life_kitchen),
        stringResource(id = R.string.text_child_clothes),
        stringResource(id = R.string.text_child_books),
        stringResource(id = R.string.text_female_clothes),
        stringResource(id = R.string.text_female_stuff),
        stringResource(id = R.string.text_male_clothes),
        stringResource(id = R.string.text_beauty_),
        stringResource(id = R.string.text_sport),
    )
    Log.d("LIST_TEST", "${list[0]} ${list[1]} ${list[2]} ${list[3]} ${list.toString()}")
    LazyColumn() {
        items(list) {item ->
            Log.d("LIST_TEST", "${item}")

            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                viewModel.setCategory(item)
                onClick() }) {
            Text(text = item.toString())
        }}

    }
}