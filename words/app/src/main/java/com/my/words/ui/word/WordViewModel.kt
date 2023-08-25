package com.my.words.ui.word

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.TimeUtils
import com.my.words.App
import com.my.words.R
import com.my.words.beans.LearnRecord
import com.my.words.beans.WordBean
import com.my.words.beans.addRecord
import com.my.words.beans.getAudioUrl
import com.my.words.ui.PlayAudio
import com.my.words.ui.PlayListener
import com.my.words.util.CacheUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class WordViewModel : ViewModel() {
    private var assetName: Int = 0
    val beanList: MutableLiveData<List<WordBean>> = MutableLiveData()
    val playIcon: MutableLiveData<Int> = MutableLiveData(R.mipmap.icon_play_1)

    var isPlaying = false

    @OptIn(DelicateCoroutinesApi::class)
    fun setAssetName(assetName: Int) {
        GlobalScope.launch {
            this@WordViewModel.assetName = assetName
            this@WordViewModel.beanList.postValue(App.getDb().word().queryAllNotRemember(assetName))
        }
        timer()

    }

    private fun cacheKey(): String {
        return "${assetName}.json"
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
    fun addLearnRecord(index: Int){
        val id = beanList.value?.get(index)?.id
        id?.let {
            addRecord(LearnRecord(0,wordId = id,TimeUtils.getNowMills()))
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

    fun cachePage(index: Int) {
        CacheUtil.setStartIndex(cacheKey(), index)
    }


//    fun getYouDaoWordBean(index: Int) {
//        if (beanList[index].youDaoWord != null) {
//            getLineInterpret(index = index, youDaoWord = beanList[index].youDaoWord?.data)
//            return
//        }
//
//        YouDaoRequest().suggest(beanList[index].name,
//            {
//                beanList[index].youDaoWord = it
//                getLineInterpret(index = index, youDaoWord = it?.data)
//            }
//        ) {
//            interpret.value = beanList[index].getLineInterpret()
//        }
//    }

//    private fun getLineInterpret(index: Int, youDaoWord: YouDaoWord.DataDTO?) {
//        val string = if (youDaoWord?.query == beanList[index].name) {
//            youDaoWord.entries?.filter { it.explain != null && it.explain != "" }
//                ?.joinToString("\n") { it.explain }
//        } else {
//            beanList[index].getLineInterpret()
//        }
//        interpret.postValue(string)
//    }

}