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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.blankj.utilcode.util.ToastUtils
import com.my.words.R
import com.my.words.beans.WordBean
import com.my.words.ui.theme.Purple80
import com.my.words.ui.theme.WordsTheme
import com.my.words.util.ThreadUtilsEx
import com.my.words.widget.RouteName
import com.my.words.widget.TopBarView

@Composable
fun WordTestPage(
    navController: NavHostController,
    wordType: String = "5",
    viewModel: WordViewModel
) {
    val beanList = viewModel.beanList.value!!
    val currentIndexState = viewModel.currentIndex.observeAsState()
    val currentIndex = currentIndexState.value!!
    viewModel.playAudio(currentIndex)
    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopBarView("${viewModel.getTitle()}:${currentIndex + 1}/${beanList.size}") {
                    navController.popBackStack()
                }
                WordView(currentIndex, viewModel) { dateBean ->

                    if (!viewModel.testNext()) {
                        navController.navigate(RouteName.HOME) {
                            popUpTo(RouteName.HOME)
                            launchSingleTop = true
                            ToastUtils.showLong("背诵完成")
                            viewModel.setLearnId(wordType, dateBean.id)
                        }
                    }
                }
            }
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun WordView(
    currentIndex: Int,
    viewModel: WordViewModel,
    wordTestViewModel: WordTestViewModel = viewModel(),
    onTestClick: (WordBean) -> Unit
) {
    val selectId = wordTestViewModel.selectId.observeAsState()

    val playIcon = viewModel.playIcon.observeAsState()
    val bean = viewModel.beanList.value!![currentIndex]
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
                    viewModel.playAudio(currentIndex)
                }
            )

        }
        Spacer(modifier = Modifier.padding(top = 20.dp))
        testData.forEach {
            SelectItem(selectId.value!!, bean, it) { dateBean, itemBean ->
                wordTestViewModel.setSelectId(itemBean.id)
                ThreadUtilsEx.runOnUiThreadDelayed(1000) { onTestClick(dateBean) }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun SelectItem(
    selectId: Int,
    dateBean: WordBean,
    itemBean: WordBean,
    onTestClick: (WordBean, WordBean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { onTestClick.invoke(dateBean, itemBean) },
            //用来设置按钮不同状态下的颜色
            colors = ButtonDefaults.buttonColors(
                containerColor = getButtonBackgroundColor(selectId, dateBean, itemBean),
            ),
            modifier = Modifier
                .width(250.dp)
                .height(55.dp)
                .align(alignment = Alignment.CenterVertically)
        ) { Text(itemBean.interpret, maxLines = 2, overflow = TextOverflow.Ellipsis) }
    }
}

fun getButtonBackgroundColor(
    selectId: Int,
    dateBean: WordBean,
    itemBean: WordBean
): Color {
    return if (selectId == itemBean.id) {
        if (selectId == dateBean.id) {
            Color.Green
        } else {
            Color.Red
        }
    } else {
        Purple80
    }
}
