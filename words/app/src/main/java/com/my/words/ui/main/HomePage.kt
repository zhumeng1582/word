package com.my.words.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.my.words.Config
import com.my.words.beans.StatisticData
import com.my.words.ui.select.SelectWordViewModel
import com.my.words.ui.theme.WordsTheme
import com.my.words.util.CacheUtil
import com.my.words.widget.HomeTopBarView
import com.my.words.widget.RouteName

@Composable
fun HomePage(navController: NavHostController, viewModel: HomeViewMode = viewModel()) {
    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>(CacheUtil.WordBookName)
        ?.observe(navController.currentBackStackEntry!!) { value ->
            viewModel.setSelectWord(value)
            viewModel.getData()
        }

    val selectWord = viewModel.selectWord.observeAsState()
    val statisticData = viewModel.statisticData.observeAsState()
    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                HomeTopBarView(selectWord.value!!) {
                    navController.navigate("selectWord")
                }
                statisticData.value?.let {
                    WordDetail(
                        it, Modifier.align(alignment = Alignment.CenterHorizontally)
                    ) {
                        navController.navigate(
                            RouteName.LEARN_PAGE.format("${viewModel.getWordLevel()}", 0)
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun WordDetail(
    statisticData: StatisticData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(modifier = modifier) {
        if (statisticData.unlearnedCount.toInt() == 0) {
            Button(
                onClick = {},
            ) {
                Text(text = "恭喜你，已完成")
            }

        } else {
            val text = if (statisticData.todayLearnAccount > 0) {
                "继续学词"
            } else {
                "背单词"
            }
            Button(
                onClick = onClick,
            ) {
                Text(text = "${text}，剩余${statisticData.unlearnedCount}个单词")
            }
        }

        Button(
            onClick = { },
        ) {
            Text(text = "今日学习:${statisticData.todayLearnAccount}个单词")
        }

        Button(
            onClick = { },
        ) {
            Text(text = "累计学习:${statisticData.accumulateLearntAccount}个单词")
        }
        Button(
            onClick = { },
        ) {
            Text(text = "累计打卡:${statisticData.accumulateAccount}天")
        }
        Button(
            onClick = { },
        ) {
            Text(text = "最大连续打卡:${statisticData.maxContinueAccount}天")
        }

    }


}