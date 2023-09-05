package com.my.words.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

object CacheUtil {
    const val WordBookName = "WordBookName"
    const val LearnWordId = "LearnWordId"
    const val LearnWordCount = "LearnWordCount"
    const val darkTheme = "darkTheme"
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

    fun getLearnWordCount(): Int {
        return MMKVManager.getInt(LearnWordCount, 5)
    }

    fun setLearnWordCount(id: Int) {
        MMKVManager.putInt(LearnWordCount, id)
    }

    @Composable
    fun isDarkTheme(): Boolean {
        return if (isSystemTheme()) {
            isSystemInDarkTheme()
        } else {
            MMKVManager.getBoolean(darkTheme, true)
        }
    }

    fun clearDarkTheme() {
        MMKVManager.remove(darkTheme)
    }

    fun isSystemTheme(): Boolean {
        return !MMKVManager.containsKey(darkTheme)
    }

    fun setDarkTheme(value: Boolean) {
        MMKVManager.putBoolean(darkTheme, value)
    }

}