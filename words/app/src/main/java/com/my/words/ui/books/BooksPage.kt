package com.my.words.ui.books

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.my.words.ui.theme.WordsTheme
import com.my.words.widget.HomeTopBarView
import com.my.words.widget.RouteName

@Composable
fun BooksPage(
    navController: NavHostController,
) {
    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                HomeTopBarView("单词本")
                Button(
                    onClick = {
                        navController.navigate(RouteName.WordListPage_S.format("ALL"))
                    },
                    Modifier
                        .width(150.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(text = "全部单词")
                }
                Button(
                    onClick = {
                        navController.navigate(RouteName.WordListPage_S.format("LEARNT"))
                    },
                    Modifier
                        .width(150.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(text = "已学单词")
                }
                Button(
                    onClick = {
                        navController.navigate(RouteName.WordListPage_S.format("ERROR"))
                    }, Modifier
                        .width(150.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(text = "生词/错词本")
                }
                Button(
                    onClick = {
                        navController.navigate(RouteName.WordListPage_S.format("DONE"))
                    }, Modifier
                        .width(150.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(text = "认识单词")
                }
            }
        }
    }
}