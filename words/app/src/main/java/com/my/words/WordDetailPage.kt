package com.my.words

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.my.words.ui.theme.WordsTheme

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WordDetailPage(navController: NavHostController, name: String) {
    val pagerState = rememberPagerState(
        //总页数
        pageCount = 1000,
        //预加载的个数
        initialOffscreenLimit = 1,
        //是否无限循环
        infiniteLoop = false,
        //初始页面
        initialPage = 0
    )
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize(),
    ) { page ->

        WordsTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "page=$page", modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                }
            }
        }
    }
}

