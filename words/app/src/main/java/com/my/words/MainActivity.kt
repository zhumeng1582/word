package com.my.words

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.my.words.ui.PlayAudio
import com.my.words.ui.main.AppScaffold
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setDecorFitsSystemWindows(true)
        setContent {
            AppScaffold()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PlayAudio.release()
    }
}

