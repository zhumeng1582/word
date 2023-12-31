package com.my.words.ui.main

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

class HomeViewMode : ViewModel() {
    init {
        getData()
    }
    val selectWord by lazy { MutableLiveData(CacheUtil.getWordBookName()) }

    val statisticData: MutableLiveData<StatisticData> = MutableLiveData()
    fun getWordLevel(): Int {
        return Config.classList.indexOf(CacheUtil.getWordBookName())
    }
    fun setSelectWord(name: String) {
        selectWord.postValue(name)
        CacheUtil.setWordBookName(name)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getData() {
        GlobalScope.launch {
            val statistic = StatisticData()
            statistic.accumulateAccount = App.getDb().record().accumulateAccount()
            statistic.accumulateLearntAccount = App.getDb().record().accumulateLearnWords()
            statistic.todayLearnAccount =
                App.getDb().record().todayLearnWords(TimerUtil.getTodayMills())
            statistic.unlearnedCount =
                App.getDb().word().unlearnedCount(getWordLevel())

            val map = HashMap<String, Int>()
            val list = App.getDb().record().getAllDayRecord()

            list.forEachIndexed { index, learnRecord ->
                val key = "" + (learnRecord.time - index * TimerUtil.dayMills)
                map[key] = map[key]?.plus(1) ?: 1
            }
            if (map.isNotEmpty()) {
                val value = map.maxBy { it.value }
                statistic.maxContinueAccount = value.value
            }
            statisticData.postValue(statistic)

        }
    }
}