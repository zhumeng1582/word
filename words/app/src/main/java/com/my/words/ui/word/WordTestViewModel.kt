package com.my.words.ui.word

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class WordTestViewModel : ViewModel() {

    val selectId: MutableLiveData<Int> = MutableLiveData(-1)
    fun setSelectId(id: Int) {
        selectId.postValue(id)
    }
}