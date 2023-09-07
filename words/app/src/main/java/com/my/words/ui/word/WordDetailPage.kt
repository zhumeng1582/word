package com.my.words.ui.word


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
import androidx.navigation.NavHostController
import com.blankj.utilcode.util.ToastUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.my.words.R
import com.my.words.beans.WordBean
import com.my.words.beans.errorCountAdd
import com.my.words.beans.getExample
import com.my.words.beans.getLineInterpret
import com.my.words.beans.setDone
import com.my.words.ui.theme.WordsTheme
import com.my.words.widget.RouteName
import com.my.words.widget.TopBarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WordDetailPage(
    navController: NavHostController,
    wordType: String = "5",
    startIndex: Int = 0,
    viewModel: WordViewModel
) {
    val beanList = viewModel.beanList.observeAsState()
    beanList.value?.let {
        if (it.isNotEmpty()) {
            WordHorizontalPager(navController, it, wordType, startIndex, viewModel)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WordHorizontalPager(
    navController: NavHostController,
    beanList: List<WordBean>,
    wordType: String,
    startIndex: Int = 0,
    viewModel: WordViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        //总页数
        pageCount = beanList.size + 1,
        //预加载的个数
        initialOffscreenLimit = 1,
        //是否无限循环
        infiniteLoop = false,
        //初始页面
        initialPage = if ((beanList.size) > startIndex) startIndex else 0
    )

    val currentIndex = pagerState.currentPage
    LaunchedEffect(currentIndex) {
        launch(Dispatchers.IO) {
            if (currentIndex < beanList.size) {
                viewModel.playAudio(currentIndex)
                viewModel.addLearnRecord(currentIndex)
            }
        }
    }
    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopBarView(getTitle(viewModel, currentIndex, pagerState)) {
                    navController.popBackStack()
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize(),
                ) { page ->
                    if (page < beanList.size) {
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
                                    if (currentIndex < pagerState.pageCount - 1) {
                                        pagerState.scrollToPage(currentIndex + 1)
                                    }
                                }
                            }

                            override fun remember(bean: WordBean) {
                                bean.setDone(true)
                                ToastUtils.showLong("已标记为认识")
                            }

                            override fun notRemember(bean: WordBean) {
                                bean.setDone(false)
                                bean.errorCountAdd()
                                ToastUtils.showLong("已标记为不认识")
                            }

                        }, viewModel)
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (viewModel.isLearnType()) {
                                Button(onClick = {
                                    navController.navigate(RouteName.WORD_TEST_D.format(wordType))
                                }) {
                                    Text(text = "开始测试")
                                }
                            } else {
                                Button(onClick = {
                                    navController.navigate(RouteName.HOME) {
                                        popUpTo(RouteName.HOME)
                                        launchSingleTop = true
                                    }
                                }) {
                                    Text(text = "完成")
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun getTitle(viewModel: WordViewModel, currentIndex: Int, pagerState: PagerState): String {

    return if (currentIndex <= pagerState.pageCount - 2) {
        "${viewModel.getTitleDetail()}:${currentIndex + 1}/${pagerState.pageCount - 1}"
    } else {
        if (viewModel.isLearnType()) {
            "开始测试"
        } else {
            "完成复习"
        }
    }

}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun WordView(
    page: Int,
    pageChangeClick: PageChangeClick,
    viewModel: WordViewModel
) {
    val playIcon = viewModel.playIcon.observeAsState()
    val bean = viewModel.beanList.value!![page]
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
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
                painter = painterResource(id = playIcon.value!!),
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
            text = bean.getLineInterpret(), fontSize = 12.sp,
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
        Text(
            text = bean.getExample(), fontSize = 12.sp,
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(start = 20.dp, end = 20.dp),
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
                pageChangeClick.remember(bean)
            }) { Text("认识") }
            Button(onClick = {
                pageChangeClick.notRemember(bean)
            }) { Text("不认识") }
        }
    }
}

interface PageChangeClick {
    fun pre()
    fun next()
    fun remember(bean: WordBean)
    fun notRemember(bean: WordBean)
}