package com.my.words.ui.select

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my.words.util.CacheUtil

class SelectWordViewModel : ViewModel() {
    val selectWord: MutableLiveData<String> = MutableLiveData(CacheUtil.getWordBookName())

    fun setSelectWord(name: String) {
        selectWord.postValue(name)
        CacheUtil.setWordBookName(name)
    }
}