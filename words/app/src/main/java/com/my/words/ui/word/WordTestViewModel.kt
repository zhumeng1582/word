package com.my.words.ui.word

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.ThreadUtils
import com.my.words.App
import com.my.words.R
import com.my.words.audio.PlayAudio
import com.my.words.audio.PlayListener
import com.my.words.beans.WordBean
import com.my.words.beans.errorCountAdd
import com.my.words.beans.getAudioUrl
import com.my.words.beans.setDone
import com.my.words.util.CacheUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class WordTestViewModel(var beanList: List<WordBean>) : ViewModel() {
    val currentIndex: MutableLiveData<Int> = MutableLiveData(0)
    val playIcon: MutableLiveData<Int> = MutableLiveData(R.mipmap.icon_play_1)
    private var isPlaying = false

    //-1未选择，-2选择不认识
    val selectId: MutableLiveData<Int> = MutableLiveData(-1)
    val testDataLiveData: MutableLiveData<MutableMap<Int, MutableList<WordBean>>> =
        MutableLiveData()

    init {
        GlobalScope.launch {
            val testData = HashMap<Int, MutableList<WordBean>>()
            beanList.forEach { it1 ->
                val index = (0..3).random()
                val list = App.getDb().word().queryTestData().toMutableList()
                list.removeIf { it.id == it1.id }

                val newList = list.subList(0, 3)
                newList.add(index, it1)
                testData[it1.id] = newList
            }
            testDataLiveData.postValue(testData)
        }
        timer()
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

    fun setSelectId(data: WordBean, item: WordBean?) {
        if (item == null || data.id != item.id) {
            data.setDone(false)
            data.errorCountAdd()
        }
        selectId.postValue(item?.id ?: -2)
    }

    fun testNext(size: Int): Boolean {
        //切换到下一个需要清空选中
        selectId.postValue(-1)

        val index = currentIndex.value ?: 0
        if (index + 1 < size) {
            currentIndex.postValue(index + 1)
            return true
        }
        return false
    }

    fun playAudio(index: Int) {
        beanList[index].let {
            PlayAudio.playAudio(it.getAudioUrl(), object : PlayListener {
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

    fun setLearnId(type: String, wordId: Int) {
        try {
            type.toInt()
            CacheUtil.setLearnWordId(wordId)
        } catch (_: NumberFormatException) {
        }
    }
}