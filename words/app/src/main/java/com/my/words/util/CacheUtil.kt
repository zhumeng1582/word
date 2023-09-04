package com.my.words.util

object CacheUtil {
    const val WordBookName = "WordBookName"
    const val LearnWordId = "LearnWordId"
    fun getWordBookName(): String {
        return MMKVManager.getString(WordBookName, "第一级")
    }

    fun setWordBookName(wordBookName: String) {
        MMKVManager.putString(WordBookName, wordBookName)
    }

    fun getLearnWordId(): Int {
        return MMKVManager.getInt(getWordBookName() + LearnWordId, 0)
    }

    fun setLearnWordId(id: Int) {
        MMKVManager.putInt(getWordBookName() + LearnWordId, id)
    }
}