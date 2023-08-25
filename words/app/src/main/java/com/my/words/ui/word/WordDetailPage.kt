package com.my.words.ui.word

import androidx.lifecycle.viewmodel.compose.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Animatable
import android.widget.ImageView
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.my.words.R
import com.my.words.beans.WordBean
import com.my.words.beans.getExample
import com.my.words.beans.getLineInterpret
import com.my.words.ui.main.SelectWord
import com.my.words.ui.main.WordDetail
import com.my.words.ui.theme.WordsTheme
import com.my.words.widget.HomeTopBarView
import com.my.words.widget.TopBarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WordDetailPage(
    navController: NavHostController,
    viewModel: WordViewModel = viewModel()
) {
    val beanList = viewModel.beanList.observeAsState()

    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        //总页数
        pageCount = beanList.value?.size ?: 0,
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
            viewModel.playAudio(currentIndex)
//            viewModel.getYouDaoWordBean(currentIndex)
        }

    }
    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopBarView("背单词:$currentIndex/${pagerState.pageCount}") {
                    navController.popBackStack()
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize(),
                ) { page ->

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
                                if (currentIndex < viewModel.beanList.value!!.size + 1) {
                                    pagerState.scrollToPage(currentIndex + 1)
                                }
                            }
                        }

                        override fun remember(bean: WordBean) {

                        }

                        override fun notRemember(bean: WordBean) {

                        }

                    })
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun WordView(
    page: Int,
    pageChangeClick: PageChangeClick,
    viewModel: WordViewModel = viewModel()
) {
//    val interpret = viewModel.interpret.observeAsState()
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