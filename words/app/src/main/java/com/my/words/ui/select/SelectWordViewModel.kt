package com.my.words.ui.select

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my.words.App
import com.my.words.Config
import com.my.words.beans.StatisticData
import com.my.words.util.CacheUtil
import com.my.words.util.TimerUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SelectWordViewModel : ViewModel() {
    val selectWord  = CacheUtil.getWordBookName()
    fun setSelectWord(name: String) {
        CacheUtil.setWordBookName(name)
    }
}