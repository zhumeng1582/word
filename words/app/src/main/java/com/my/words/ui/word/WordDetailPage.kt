package com.my.words.ui.word
import androidx.lifecycle.viewmodel.compose.viewModel

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.my.words.R
import com.my.words.ui.theme.WordsTheme
import com.my.words.widget.TopBarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WordDetailPage(
    viewModel: WordViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        //总页数
        pageCount = viewModel.beanList.size,
        //预加载的个数
        initialOffscreenLimit = 1,
        //是否无限循环
        infiniteLoop = false,
        //初始页面
        initialPage = viewModel.startPageIndex
    )
    val currentIndex = pagerState.currentPage
    LaunchedEffect(currentIndex) {
        launch(Dispatchers.IO) {
            viewModel.cachePage(currentIndex)
            viewModel.initPlay(currentIndex)
            viewModel.getYouDaoWordBean(currentIndex)
        }

    }

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
                WordView(page, object : PageChangeClick {
                    override fun pre() {
                        coroutineScope.launch {
                            if (currentIndex > 1) {
                                pagerState.scrollToPage(currentIndex - 1)
                            }
                        }
                    }

                    override fun next() {
                        coroutineScope.launch {
                            if (currentIndex < viewModel.beanList.size + 1) {
                                pagerState.scrollToPage(currentIndex + 1)

                            }
                        }
                    }

                })
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun WordView(page: Int, pageChangeClick: PageChangeClick, viewModel: WordViewModel = viewModel()) {
    val interpret = viewModel.interpret.observeAsState()

    val bean = viewModel.beanList[page]

    Column(modifier = Modifier.fillMaxWidth()) {
        TopBarView("",{})
        Text(
            text = "当前单词：$page",
            modifier = Modifier
                .align(alignment = Alignment.End)
                .padding(20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            horizontalArrangement = Arrangement.Center,//设置水平居中对齐
            verticalAlignment = Alignment.CenterVertically
        ) {//设置垂直居中对齐
            Text(
                text = bean.name,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue,
                modifier = Modifier.padding(end = 10.dp)
            )
            Image(
                painter = painterResource(id = R.mipmap.icon_play_1),
                contentDescription = stringResource(id = R.string.icon_play),
                modifier = Modifier.clickable {
                    viewModel.playAudio(page)
                }
            )

        }

        Text(
            text = bean.phonetic,
            fontStyle = FontStyle.Italic,
            fontSize = 26.sp,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )

        Text(
            text = interpret.value ?: "", fontSize = 12.sp,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(20.dp),
            style = LocalTextStyle.current.merge(
                TextStyle(
                    lineHeight = 1.5.em,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    )
                )
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                pageChangeClick.pre()
            }) { Text("上一个") }
            Button(onClick = {
                pageChangeClick.next()
            }) { Text("下一个") }
        }
    }
}

interface PageChangeClick {
    fun pre()
    fun next()
}