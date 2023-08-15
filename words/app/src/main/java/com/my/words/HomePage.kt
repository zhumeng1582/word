package com.my.words

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.my.words.ui.theme.WordsTheme

@Composable
fun HomePage(navController: NavHostController) {
    val classList = listOf("第一级","第二级","第三级","第四级","第五级","第六级","第七级","第八级","第九级","第十级","第十一级","第十二级")

    WordsTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                classList.forEach { name ->
                    Greeting(navController,name = name,modifier = Modifier
                        .width(150.dp)
                        .align(alignment = Alignment.CenterHorizontally))

                }
            }
        }
    }

}
@Composable
fun Greeting(navController: NavHostController,name: String, modifier: Modifier = Modifier) {
    Button(onClick = { navController.navigate("detail/$name") }, modifier = modifier) {
        Text(text = name)
    }
}

