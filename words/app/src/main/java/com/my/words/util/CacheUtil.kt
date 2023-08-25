package com.my.words.util

object CacheUtil {
    const val WordBookName = "WordBookName"
    fun getWordBookName(): String {
        return MMKVManager.getString(WordBookName, "第一级")!!
    }

    fun setWordBookName(wordBookName: String) {
        MMKVManager.putString(WordBookName, wordBookName)
    }

    fun getStartIndex(key: String): Int {
        return MMKVManager.getInt(key, 0)
    }

    fun setStartIndex(key: String, index: Int) {
        MMKVManager.putInt(key, index)
    }
}