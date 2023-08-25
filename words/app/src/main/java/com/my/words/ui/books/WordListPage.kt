package com.my.words.ui.books

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.my.words.beans.WordBean
import com.my.words.ui.theme.WordsTheme
import com.my.words.widget.TopBarView

@Composable
fun WordListPage(
    navController: NavHostController,
    viewModel: WordListViewModel = viewModel()
) {
    val list = viewModel.beanList.observeAsState()

    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopBarView(viewModel.typeTitle) {
                    navController.popBackStack()
                }
                list.value?.let { WordList(it) }
            }

        }
    }
}

@Composable
fun WordList(messages: List<WordBean>) {
    LazyColumn {
        items(messages.size) { index ->
            Text(text = messages[index].name)
        }
    }
}

