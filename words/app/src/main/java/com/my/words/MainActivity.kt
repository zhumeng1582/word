package com.my.words

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.my.words.ui.PlayAudio
import com.my.words.ui.word.WordDetailPage

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainPage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PlayAudio.release()
    }
}

@Composable
fun MainPage() {
    val navController= rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomePage(navController) }
        composable("detail/{index}") { it.arguments?.getString("index")
            ?.let { index -> WordDetailPage(navController, index) } }
    }
}

