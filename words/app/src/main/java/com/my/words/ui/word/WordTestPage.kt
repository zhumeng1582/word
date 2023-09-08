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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import com.my.words.ui.theme.Purple80
import com.my.words.ui.theme.WordsTheme
import com.my.words.util.ThreadUtilsEx
import com.my.words.widget.RouteName
import com.my.words.widget.TopBarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WordTestPage(
    navController: NavHostController,
    wordType: String = "5",
    wordTestViewModel: WordTestViewModel
) {
    val currentIndexState = wordTestViewModel.currentIndex.observeAsState()
    val currentIndex = currentIndexState.value!!
    LaunchedEffect(currentIndex) {
        launch(Dispatchers.IO) {
            if (currentIndex < wordTestViewModel.beanList.size) {
                wordTestViewModel.playAudio(currentIndex)
            }
        }
    }

    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopBarView(
                    "单词测试:${currentIndex + 1}/${wordTestViewModel.beanList.size}"
                ) {
                    navController.popBackStack()
                }
                WordView(currentIndex, wordTestViewModel) { dateBean ->

                    if (!wordTestViewModel.testNext(wordTestViewModel.beanList.size)) {
                        navController.navigate(RouteName.HOME) {
                            popUpTo(RouteName.HOME)
                            launchSingleTop = true
                            ToastUtils.showLong("背诵完成")
                            wordTestViewModel.setLearnId(wordType, dateBean.id)
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
    wordTestViewModel: WordTestViewModel,
    onTestClick: (WordBean) -> Unit
) {
    val selectId = wordTestViewModel.selectId.observeAsState()

    val playIcon = wordTestViewModel.playIcon.observeAsState()
    val bean = wordTestViewModel.beanList[currentIndex]
    val testData = wordTestViewModel.testDataLiveData.observeAsState()
    testData.value?.get(bean.id)?.let {
        testItemPage(bean, playIcon, wordTestViewModel, currentIndex, it, selectId, onTestClick)
    }
}

@Composable
private fun testItemPage(
    bean: WordBean,
    playIcon: State<Int?>,
    wordTestViewModel: WordTestViewModel,
    currentIndex: Int,
    testList: List<WordBean>,
    selectId: State<Int?>,
    onTestClick: (WordBean) -> Unit
) {
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
                    wordTestViewModel.playAudio(currentIndex)
                }
            )

        }
        Spacer(modifier = Modifier.padding(top = 20.dp))
        testList.forEach {
            SelectItem(selectId.value!!, bean, it) { dateBean, itemBean ->
                wordTestViewModel.setSelectId(dateBean, itemBean)
                ThreadUtilsEx.runOnUiThreadDelayed(1000) { onTestClick(dateBean) }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    wordTestViewModel.setSelectId(bean, null)
                    ThreadUtilsEx.runOnUiThreadDelayed(1000) { onTestClick(bean) }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple80,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .height(55.dp)
                    .align(alignment = Alignment.CenterVertically)
            ) { Text("In-cognizance", maxLines = 2, overflow = TextOverflow.Ellipsis) }
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
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { onTestClick.invoke(dateBean, itemBean) },
            colors = ButtonDefaults.buttonColors(
                containerColor = getButtonBackgroundColor(selectId, dateBean, itemBean),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)
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
    return if (selectId == -1) {
        Purple80
    } else {
        if (itemBean.id == dateBean.id) {
            Color(0xFF00BA3D)
        } else if (selectId == itemBean.id) {
            Color(0xFFE55D75)
        } else {
            Purple80
        }
    }
}
