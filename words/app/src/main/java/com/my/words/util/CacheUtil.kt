package com.my.words.util

import com.blankj.utilcode.util.CacheDiskStaticUtils

object CacheUtil {
    const val WordBookName = "WordBookName"
    fun getWordBookName(): String {
        return CacheDiskStaticUtils.getString(WordBookName, "第一级")
    }

    fun setWordBookName(wordBookName: String) {
        CacheDiskStaticUtils.put(WordBookName, wordBookName)
    }

    fun getStartIndex(key: String): Int {
        return CacheDiskStaticUtils.getString(key, "0").toInt()
    }

    fun setStartIndex(key: String, index: Int) {
        CacheDiskStaticUtils.put(key, "$index")
    }
}