package com.my.words.ui.books

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.my.words.beans.WordBean
import com.my.words.ui.theme.WordsTheme
import com.my.words.widget.RouteName
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
                list.value?.let { WordList(navController,it) }
            }

        }
    }
}

@Composable
fun WordList(navController: NavHostController,messages: List<WordBean>,viewModel: WordListViewModel = viewModel()) {
    LazyColumn {
        items(messages.size) { index ->
            Box(modifier = Modifier.clickable {
                navController.navigate(RouteName.DETAIL_S.format(viewModel.type))
                // 处理点击事件
            }) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),) {
                    Column {
                        Text(text = messages[index].name,
                            fontSize = 24.sp,
                            color = Color.Blue)
                        Text(text = messages[index].interpret, fontSize = 12.sp)
                    }
                }
                Divider()//添加分割线
            }


        }
    }
}

