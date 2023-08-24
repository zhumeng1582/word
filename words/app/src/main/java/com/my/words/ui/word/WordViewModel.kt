package com.my.words.ui.word

import android.app.Activity
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.ThreadUtils
import com.my.words.App
import com.my.words.R
import com.my.words.beans.WordBean
import com.my.words.beans.YouDaoWord
import com.my.words.beans.getAudioUrl
import com.my.words.beans.getLineInterpret
import com.my.words.network.YouDaoRequest
import com.my.words.ui.PlayAudio
import com.my.words.ui.PlayListener
import com.my.words.util.CacheUtil
import com.my.words.util.ThreadUtilsEx
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class WordViewModel : ViewModel() {
    private var assetName: Int = 0
    lateinit var beanList: List<WordBean>
    var startPageIndex by Delegates.notNull<Int>()
    var isPlaying = false

    fun setAssetName(assetName: Int) {
        this.assetName = assetName
        this.beanList = getList()
        this.startPageIndex = CacheUtil.getStartIndex(cacheKey())
    }

    private fun cacheKey(): String {
        return "${assetName}.json"
    }

    val interpret: MutableLiveData<String> = MutableLiveData("")
    val playIcon: MutableLiveData<Int> = MutableLiveData(R.mipmap.icon_play_1)

    private fun getList(): List<WordBean> {
        timer()
        val json = ResourceUtils.readAssets2String(cacheKey())
        return GsonUtils.fromJson(json, GsonUtils.getListType(WordBean::class.java))
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

    fun playAudio(activity: Activity,index: Int) {
        val proxy = App.getProxy(activity)
        val proxyUrl = proxy.getProxyUrl(beanList[index].getAudioUrl())
        PlayAudio.play(proxyUrl,object :PlayListener{
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

    fun getYouDaoWordBean(index: Int) {
        if (beanList[index].youDaoWord != null) {
            getLineInterpret(index = index, youDaoWord = beanList[index].youDaoWord?.data)
            return
        }

        YouDaoRequest().suggest(beanList[index].name,
            {
                beanList[index].youDaoWord = it
                getLineInterpret(index = index, youDaoWord = it?.data)
            }
        ) {
            interpret.value = beanList[index].getLineInterpret()
        }
    }

    private fun getLineInterpret(index: Int, youDaoWord: YouDaoWord.DataDTO?) {
        val string = if (youDaoWord?.query == beanList[index].name) {
            youDaoWord.entries?.filter { it.explain != null && it.explain != "" }
                ?.joinToString("\n") { it.explain }
        } else {
            beanList[index].getLineInterpret()
        }
        interpret.postValue(string)
    }

}