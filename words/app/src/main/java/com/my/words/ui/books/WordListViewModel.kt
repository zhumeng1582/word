package com.my.words.ui.books

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my.words.App
import com.my.words.Config
import com.my.words.beans.WordBean
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WordListViewModel : ViewModel() {
    val beanList: MutableLiveData<List<WordBean>> = MutableLiveData()
    var typeTitle: String = ""

    @OptIn(DelicateCoroutinesApi::class)
    fun setListType(type: String) {
        GlobalScope.launch {
            this@WordListViewModel.beanList.postValue(getList(type))
        }
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
                App.getDb().record().queryAll()
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
}