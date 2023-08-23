package com.my.words.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarView(title: String, callback: () -> Unit) {
    Column {
        TopAppBar(modifier = Modifier.statusBarsPadding(), title = {
            Text(title)
        }, navigationIcon = {
            IconButton(onClick = {
                callback()
            }) {
                Icon(Icons.Filled.ArrowBack, "")
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBarView(title: String) {
    CenterAlignedTopAppBar(modifier = Modifier.statusBarsPadding(),
        colors= TopAppBarDefaults.smallTopAppBarColors(),
        title = { Text(title) }
    )
}
