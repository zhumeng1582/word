package com.my.words.ui.select

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils
import com.my.words.App
import com.my.words.util.CacheUtil
import com.my.words.util.TimerUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SelectWordViewModel : ViewModel() {
    val selectWord by lazy { MutableLiveData(CacheUtil.getWordBookName()) }
    val accumulateAccount: MutableLiveData<Int> = MutableLiveData(0)
    val accumulateLearntAccount: MutableLiveData<Int> = MutableLiveData(0)
    val maxContinueAccount: MutableLiveData<Int> = MutableLiveData(0)
    fun setSelectWord(name: String) {
        selectWord.postValue(name)
        CacheUtil.setWordBookName(name)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getData() {
        GlobalScope.launch {
            val count = App.getDb().record().accumulateAccount()
            accumulateAccount.postValue(count)

            val countWords = App.getDb().record().accumulateLearnWords()
            accumulateLearntAccount.postValue(countWords)

            val map = HashMap<String, Int>()
            val list = App.getDb().record().accumulateContinueAccount()

            list.forEachIndexed { index, learnRecord ->
                val key = "" + (learnRecord.time - index * TimerUtil.dayMills)
                map[key] = map[key]?.plus(1) ?: 1
            }
            if (map.isNotEmpty()) {
                val value = map.maxBy { it.value }
                maxContinueAccount.postValue(value.value)
            }

        }
    }

}