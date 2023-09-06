package com.my.words.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.navigation.NavHostController
import com.blankj.utilcode.util.ToastUtils
import com.my.words.ui.theme.WordsTheme
import com.my.words.util.CacheUtil
import com.my.words.widget.TopBarView
import com.my.words.widget.numberpicker.NumberPicker

@Composable
fun Setting(
    navController: NavHostController,
    settingMode: SettingMode = SettingMode(CacheUtil.isDarkTheme(), isSystemInDarkTheme())
) {
    val openDialog = remember { mutableStateOf(false) }
    val isDarkTheme = settingMode.isDarkTheme.observeAsState()
    val isSystemInDarkTheme = settingMode.isSystemInDarkTheme.observeAsState()

    WordsTheme(darkTheme = isDarkTheme.value!!) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopBarView("设置") {
                    navController.popBackStack()
                }
                Button(onClick = {
                    settingMode.clearRecord()
                }) {
                    Text(text = "重置学习记录")
                }
                Button(onClick = {
                    openDialog.value = true
                }) {
                    Text(text = "修改学习计划")
                }
                Text(text = "夜间模式")
                Row {
                    Switch(
                        checked = isSystemInDarkTheme.value!!,
                        onCheckedChange = {
                            settingMode.setIsSystemInDarkTheme(it)
                        }
                    )
                    Text(text = if (isSystemInDarkTheme.value == true) "当前是跟随系统" else "当前是不跟随系统")
                }
                Row {
                    Switch(
                        checked = isDarkTheme.value!!,
                        enabled = !isSystemInDarkTheme.value!!,
                        onCheckedChange = {
                            settingMode.setDarkTheme(it)
                        }
                    )
                    Text(text = if (isDarkTheme.value == true) "深色模式" else "浅色模式")
                }

            }
            if (openDialog.value) {
                dialogLearnPlan(openDialog,settingMode)
            }
        }
    }

}


@Composable
fun dialogLearnPlan(state: MutableState<Boolean>, settingMode: SettingMode) {
    var pickerValue by remember { mutableIntStateOf(settingMode.getLearnWordCount()) }
    Dialog(
        onDismissRequest = {
            state.value = false
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            securePolicy = SecureFlagPolicy.SecureOff
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 15.dp)
                .background(
                    Color.White, shape = RoundedCornerShape(8.dp)
                )
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "修改学习计划",
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold
            )

            NumberPicker(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally),
                value = pickerValue,
                range = 5..20,
                onValueChange = {
                    pickerValue = it
                }
            )
            Divider(
                modifier = Modifier
                    .height(0.5.dp)
                    .background(color = Color.Gray)
            )
            Row {
                Button(
                    onClick = {
                        state.value = false
                    },
                    modifier = Modifier.weight(1f, true),
                    shape = RoundedCornerShape(bottomStart = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                ) {
                    Text(text = "取消", color = Color.Gray)
                }
                Divider(modifier = Modifier.width(0.5.dp))
                Button(
                    onClick = {
                        state.value = false
                        settingMode.setLearnWordCount(pickerValue)
                        ToastUtils.showLong("修改成功")
                    },
                    modifier = Modifier.weight(1f, true),
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                ) {
                    Text(text = "确定", color = Color.Green)
                }
            }

        }
    }
}
