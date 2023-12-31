package com.my.words.ui.books

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.my.words.beans.WordBean
import com.my.words.ui.theme.WordsTheme
import com.my.words.ui.word.WordViewModel
import com.my.words.widget.RouteName
import com.my.words.widget.TopBarView

@Composable
fun WordListPage(
    navController: NavHostController,
    viewModel: WordViewModel
) {
    val list = viewModel.beanList.observeAsState()

    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                list.value?.let { it1 ->
                    val listState = rememberLazyListState(
                        initialFirstVisibleItemIndex = viewModel.getLearnWordIdIndex(),
                        initialFirstVisibleItemScrollOffset = 0
                    )
                    TopBarView(viewModel.getTitle(), actions = {
                        IconButton(onClick = {
                            viewModel.export(context = navController.context)
                        }) {
                            Icon(Icons.Filled.Share, null)
                        }
                    }) {
                        navController.popBackStack()
                    }
                    WordList(it1, listState, viewModel) {
                        navController.navigate(RouteName.DETAIL_D.format(viewModel.type,it))
                    }
                }
            }

        }
    }
}

@Composable
fun WordList(
    messages: List<WordBean>,
    listState: LazyListState,
    viewModel: WordViewModel,
    onClick: (Int) -> Unit
) {
    LazyColumn(state = listState) {
        items(messages.size) { index ->
            Box(modifier = Modifier.clickable {
                onClick.invoke(index)
            }) {
                if (viewModel.getLearnWordId() == messages[index].id) {
                    Text(
                        text = "当前学习位置",
                        fontSize = 12.sp,
                        color = Color.Green,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, top = 5.dp),
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                ) {
                    Column {

                        Text(
                            text = messages[index].name,
                            fontSize = 24.sp,
                            color = Color.Blue
                        )
                        Text(text = messages[index].interpret, fontSize = 12.sp)
                    }
                }
                Divider()//添加分割线
            }


        }
    }
}

