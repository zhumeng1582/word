package com.my.words

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.my.words.ui.PlayAudio
import com.my.words.ui.main.AppScaffold
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScaffold()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PlayAudio.release()
    }
}

