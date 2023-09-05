package com.my.words.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my.words.util.CacheUtil

class SettingMode(darkTheme: Boolean, private var systemDark: Boolean) : ViewModel() {

    var isDarkTheme: MutableLiveData<Boolean> = MutableLiveData(darkTheme)
    var isSystemInDarkTheme: MutableLiveData<Boolean> =
        MutableLiveData(CacheUtil.isSystemTheme())

    fun clearRecord() {
        CacheUtil.setLearnWordId(0)
    }

    fun setDarkTheme(value: Boolean) {
        isDarkTheme.postValue(value)
        CacheUtil.setDarkTheme(value)
    }

    fun setIsSystemInDarkTheme(value: Boolean) {
        isSystemInDarkTheme.postValue(value)

        if (value) {
            CacheUtil.clearDarkTheme()
            isDarkTheme.postValue(systemDark)
        } else {
            CacheUtil.setDarkTheme(isDarkTheme.value!!)
        }
    }

    fun getLearnWordCount(): Int {
        return CacheUtil.getLearnWordCount()
    }

    fun setLearnWordCount(count: Int) {
        CacheUtil.setLearnWordCount(count)
    }

}