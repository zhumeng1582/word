package com.my.words.ui.profile

import androidx.lifecycle.ViewModel
import com.my.words.util.CacheUtil

class SettingMode : ViewModel() {
    fun clearRecord() {
        CacheUtil.setLearnWordId(0)
    }

    fun getLearnWordCount(): Int {
        return CacheUtil.getLearnWordCount()
    }

    fun setLearnWordCount(count: Int) {
        CacheUtil.setLearnWordCount(count)
    }

}