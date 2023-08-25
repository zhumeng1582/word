package com.my.words.ui.main

import androidx.compose.foundation.layout.Arrangement
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.my.words.Config
import com.my.words.ui.select.SelectWordViewModel
import com.my.words.ui.theme.WordsTheme
import com.my.words.util.CacheUtil
import com.my.words.widget.HomeTopBarView
import com.my.words.widget.RouteName

@Composable
fun HomePage(navController: NavHostController, viewModel: SelectWordViewModel = viewModel()) {
    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>(CacheUtil.WordBookName)
        ?.observe(navController.currentBackStackEntry!!) { value ->
            viewModel.setSelectWord(value)
        }
    val interpret = viewModel.selectWord.observeAsState()

    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                HomeTopBarView(interpret.value!!){
                    navController.navigate("selectWord")
                }

                WordDetail(
                    navController, Modifier
                        .width(150.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }
        }
    }
}


@Composable
fun WordDetail(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SelectWordViewModel = viewModel()
) {
    val interpret = viewModel.selectWord.observeAsState()

    val index = Config.classList.indexOf(interpret.value)

    Button(
        onClick = { navController.navigate(RouteName.DETAIL_D.format(index)) },
        modifier = modifier
    ) {
        Text(text = "背单词")
    }
}