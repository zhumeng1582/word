package com.my.words.ui.select

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.my.words.Config
import com.my.words.ui.theme.WordsTheme
import com.my.words.util.CacheUtil
import com.my.words.widget.TopBarView

@Composable
fun SelectWordBookPage(navController: NavHostController) {

    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopBarView("") { navController.popBackStack() }
                Config.classList.forEach { name ->
                    Greeting(
                        navController, name, Modifier
                            .width(150.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                    )

                }
            }
        }
    }

}

@Composable
fun Greeting(
    navController: NavHostController,
    name: String,
    modifier: Modifier = Modifier,
    viewModel: SelectWordViewModel = viewModel()
) {

    Row(modifier = modifier) {
        RadioButton(
            selected = name == viewModel.selectWord,
            onClick = {
                viewModel.setSelectWord(name)
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    CacheUtil.WordBookName,
                    name
                )
                navController.popBackStack()
            }
        )
        Text(text = name)
    }
}

