package com.my.words.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.my.words.ui.theme.WordsTheme
import com.my.words.widget.HomeTopBarView
import com.my.words.widget.RouteName

@Composable
fun Profile(navController: NavHostController) {
    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                HomeTopBarView("交流电") {

                }
                Button(onClick = { }) {
                    Text(text = "打卡日历")
                }
                Button(onClick = { }) {
                    Text(text = "学习记录")
                }
                Button(onClick = {
                    navController.navigate(RouteName.SETTING)
                }) {
                    Text(text = "设置")
                }
            }
        }
    }
}