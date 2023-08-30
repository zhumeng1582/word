package com.my.words.ui.webview

import android.os.Build
import android.webkit.WebSettings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.my.words.ui.theme.WordsTheme

@Composable
fun WebViewPage(
    navController: NavHostController,url:String
) {
    val state = rememberWebViewState(url)
    WordsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                WebView(state, onCreated = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        it.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                    }
                    it.settings.javaScriptEnabled = true
                }).apply {


                }
            }
        }
    }

}