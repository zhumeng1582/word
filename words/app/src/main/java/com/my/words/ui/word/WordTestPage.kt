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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.blankj.utilcode.util.ToastUtils
import com.my.words.R
import com.my.words.beans.WordBean
import com.my.words.ui.theme.WordsTheme
import com.my.words.widget.RouteName
import com.my.words.widget.TopBarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WordTestPage(
    navController: NavHostController,
    wordType: String = "5",
    viewModel: WordViewModel
) {
    val beanList = viewModel.beanList.value
    val currentIndexState = viewModel.currentIndex.observeAsState()
    val currentIndex = currentIndexState.value!!
    viewModel.playAudio(currentIndex)

    beanList?.let {
        WordTestPager(navController, it, currentIndex, viewModel) {
            if (!viewModel.testNext()) {
                navController.navigate(RouteName.HOME) {
                    popUpTo(RouteName.HOME)
                    launchSingleTop = true

                    ToastUtils.showLong("背诵完成")
                    viewModel.setLearnId(wordType, currentIndex)
                }
            }
        }
    }
}

@Composable
fun WordTestPager(
    navController: NavHostController,
    beanList: List<WordBean>,
    currentIndex: Int,
    viewModel: WordViewModel,
    next: () -> Unit
) {

    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopBarView("${viewModel.getTitle()}:${currentIndex + 1}/${beanList.size}") {
                    navController.popBackStack()
                }
                WordView(currentIndex, viewModel) {
                    viewModel.setSelectTestData(beanList[currentIndex].id, it.id)
                    next()
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun WordView(
    page: Int,
    viewModel: WordViewModel,
    onTestClick: (WordBean) -> Unit
) {
    val playIcon = viewModel.playIcon.observeAsState()
    val bean = viewModel.beanList.value!![page]
    val testData = viewModel.testData[bean.id]!!
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
        Spacer(modifier = Modifier.padding(top = 20.dp))
        SelectItem(testData[0], onTestClick)
        SelectItem(testData[1], onTestClick)
        SelectItem(testData[2], onTestClick)
        SelectItem(testData[3], onTestClick)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun SelectItem(
    testBean: WordBean,
    onTestClick: (WordBean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { onTestClick.invoke(testBean) },
            modifier = Modifier
                .width(250.dp)
                .height(55.dp)
                .align(alignment = Alignment.CenterVertically)
        ) { Text(testBean.interpret, maxLines = 2, overflow = TextOverflow.Ellipsis) }
    }
}
