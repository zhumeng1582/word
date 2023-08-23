package com.my.words.ui.word

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ResourceUtils
import com.my.words.beans.WordBean
import com.my.words.beans.YouDaoWord
import com.my.words.beans.getAudioUrl
import com.my.words.beans.getLineInterpret
import com.my.words.network.YouDaoRequest
import com.my.words.ui.PlayAudio
import com.my.words.util.CacheUtil
import kotlin.properties.Delegates

class WordViewModel : ViewModel() {
    private var assetName: Int = 0
    lateinit var beanList: List<WordBean>
    var startPageIndex by Delegates.notNull<Int>()

    fun setAssetName(assetName: Int) {
        this.assetName = assetName
        this.beanList = getList()
        this.startPageIndex = CacheUtil.getStartIndex(cacheKey())
    }

    private fun cacheKey(): String {
        return "${assetName}.json"
    }

    val interpret: MutableLiveData<String> = MutableLiveData("")


    private fun getList(): List<WordBean> {
        val json = ResourceUtils.readAssets2String(cacheKey())
        return GsonUtils.fromJson(json, GsonUtils.getListType(WordBean::class.java))
    }

    fun playAudio(index: Int) {
        PlayAudio.play(beanList[index].getAudioUrl())
    }

    fun initPlay(index: Int) {
        PlayAudio.initPlayer(beanList[index].getAudioUrl())
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