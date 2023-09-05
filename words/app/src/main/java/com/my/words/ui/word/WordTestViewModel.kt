package com.my.words.ui.word

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my.words.beans.WordBean
import com.my.words.beans.errorCountAdd
import com.my.words.beans.setDone

class WordTestViewModel : ViewModel() {
    val currentIndex: MutableLiveData<Int> = MutableLiveData(0)

    val selectId: MutableLiveData<Int> = MutableLiveData(-1)
    fun setSelectId(data: WordBean, item: WordBean) {
        if (data.id != item.id) {
            data.setDone(false)
            data.errorCountAdd()
        }
        selectId.postValue(item.id)
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
}