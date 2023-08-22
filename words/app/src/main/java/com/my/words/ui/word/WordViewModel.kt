package com.my.words.ui.word

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ResourceUtils
import com.my.words.beans.WordBean
import com.my.words.beans.YouDaoWord
import com.my.words.beans.getLineInterpret
import com.my.words.network.YouDaoRequest
import com.my.words.ui.PlayAudio

class WordViewModel(assetName: Int) : ViewModel() {
    private val cacheKey = "${assetName}json"
    val interpret: MutableLiveData<String> = MutableLiveData("")

    val beanList = getList(assetName)
    val startPageIndex = CacheDiskStaticUtils.getString(cacheKey, "0").toInt()

    private fun getList(assetName: Int): List<WordBean> {
        val json = ResourceUtils.readAssets2String("${assetName + 1}.json")
        return GsonUtils.fromJson(json, GsonUtils.getListType(WordBean::class.java))
    }

    fun playAudio() {
        PlayAudio.play()
    }

    fun initPlay(index: Int) {
        PlayAudio.initPlayer("https://dict.youdao.com/dictvoice?audio=${beanList[index].name}")
    }

    fun cachePage(index: Int) {
        CacheDiskStaticUtils.put(cacheKey, "$index")
    }

    fun getYouDaoWordBean(index: Int) {
        YouDaoRequest().suggest(beanList[index].name,
            {
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
        interpret.value = string
    }

}