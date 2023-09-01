package com.my.words.ui.word

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.ThreadUtils
import com.my.words.App
import com.my.words.Config
import com.my.words.R
import com.my.words.beans.LearnRecord
import com.my.words.beans.WordBean
import com.my.words.beans.addRecord
import com.my.words.beans.getAudioUrl
import com.my.words.ui.PlayAudio
import com.my.words.ui.PlayListener
import com.my.words.util.TimerUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class WordViewModel : ViewModel() {
    val beanList: MutableLiveData<List<WordBean>> = MutableLiveData()
    val playIcon: MutableLiveData<Int> = MutableLiveData(R.mipmap.icon_play_1)
    var isPlaying = false
    var typeTitle = "背单词"

    @OptIn(DelicateCoroutinesApi::class)
    fun setListType(type: String){
        GlobalScope.launch {
            when (type) {
                "LEARNT", "ERROR", "DONE", "ALL" -> {
                    this@WordViewModel.beanList.postValue(getList(type))
                }
                else -> {
                    this@WordViewModel.beanList.postValue(App.getDb().word().queryNotDoneLimit20(type.toInt()))
                }
            }
            println("------------>setListType")
        }
        timer()
    }

    private fun getList(type: String): List<WordBean> {
        this.typeTitle = when (type) {
            "LEARNT" -> "已学习单词"
            "ERROR" -> "错误单词"
            "DONE" -> "已完成单词"
            else -> "全部单词"
        }
        return when (type) {
            "LEARNT" -> {
                App.getDb().record().queryAllLearnDistinct()
            }

            "ERROR" -> {
                App.getDb().word().queryAllError()
            }

            "DONE" -> {
                App.getDb().word().queryAllDone()
            }

            else -> {
                App.getDb().word().query(Config.getBookIndex())
            }
        }
    }

    private fun timer() {
        val task: ThreadUtils.SimpleTask<Int> = object : ThreadUtils.SimpleTask<Int>() {
            override fun doInBackground(): Int {
                return 0
            }

            override fun onSuccess(result: Int) {
                if (isPlaying) {
                    val nextIcon = when (playIcon.value) {
                        R.mipmap.icon_play_1 -> R.mipmap.icon_play_4
                        R.mipmap.icon_play_2 -> R.mipmap.icon_play_1
                        R.mipmap.icon_play_3 -> R.mipmap.icon_play_2
                        R.mipmap.icon_play_4 -> R.mipmap.icon_play_3
                        else -> R.mipmap.icon_play_1
                    }
                    playIcon.postValue(nextIcon)
                }

            }
        }
        ThreadUtils.executeByCachedAtFixRate(task, 800, TimeUnit.MILLISECONDS)
    }

    fun addLearnRecord(index: Int) {
        val id = beanList.value?.get(index)?.id
        id?.let {
            addRecord(LearnRecord(wordId = id, TimerUtil.getTodayMills()))
        }
    }

    fun playAudio(index: Int) {
        val proxy = App.getProxy()
        val proxyUrl = proxy.getProxyUrl(beanList.value?.get(index)?.getAudioUrl() ?: "")
        PlayAudio.play(proxyUrl, object : PlayListener {
            override fun play() {
                isPlaying = true
            }

            override fun end() {
                isPlaying = false
                playIcon.postValue(R.mipmap.icon_play_1)
            }

        })
    }
}