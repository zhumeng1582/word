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
import com.my.words.util.CacheUtil
import com.my.words.util.TimerUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class WordViewModel : ViewModel() {
    val beanList: MutableLiveData<List<WordBean>> = MutableLiveData(arrayListOf())
    val testData: MutableMap<Int, MutableList<WordBean>> = LinkedHashMap()
    val playIcon: MutableLiveData<Int> = MutableLiveData(R.mipmap.icon_play_1)
    private var isPlaying = false
    var type = ""

    @OptIn(DelicateCoroutinesApi::class)
    fun setListType(type: String) {
        this.type = type
        this@WordViewModel.beanList.postValue(arrayListOf())
        GlobalScope.launch {
            when (type) {
                "LEARNT", "ERROR", "DONE", "ALL" -> {
                    this@WordViewModel.beanList.postValue(getList(type))
                }

                else -> {
                    this@WordViewModel.beanList.postValue(
                        App.getDb().word()
                            .queryAllNotLearn(type.toInt(), CacheUtil.getLearnWordId())
                    )
                    this@WordViewModel.beanList.value?.forEach { it1 ->
                        val index = (0..3).random()
                        val list = App.getDb().word().queryTestData().toMutableList()
                        list.removeIf { it.id == it1.id }

                        val newList = list.subList(0, 3)
                        newList.add(index, it1)
                        testData[it1.id] = newList
                    }
                }
            }

        }
        timer()
    }


    //是学习类型还是复习类型
    fun isLearnType(): Boolean {
        return try {
            type.toInt()
            true
        } catch (_: NumberFormatException) {
            false
        }
    }


    fun setLearnId(type: String, wordId: Int) {
        try {
            type.toInt()
            CacheUtil.setLearnWordId(wordId)
        } catch (_: NumberFormatException) {
        }
    }

    fun getTitle(): String {
        return when (type) {
            "LEARNT" -> "已学习单词:${beanList.value?.size}"
            "ERROR" -> "错误单词:${beanList.value?.size}"
            "DONE" -> "已完成单词:${beanList.value?.size}"
            "ALL" -> "全部单词:${getLearnSize()}/${beanList.value?.size}"
            else -> "学习单词"
        }
    }

    fun getLearnWordId(): Int {
        return CacheUtil.getLearnWordId()
    }

    private fun getLearnSize(): Int {
        return beanList.value?.count { it.id <= CacheUtil.getLearnWordId() } ?: 0
    }

    private fun getList(type: String): List<WordBean> {

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