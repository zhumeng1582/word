package com.my.words.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.my.words.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Entity
data class WordBean(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    var level: Int = 0,
    val phonetic: String,
    val sound: String,
    val interpret: String,
    val example: String,
    var isDone: Boolean = false,
    var errorCount: Int = 0,
)

fun WordBean.setDown(isDone:Boolean) {
    this.isDone = isDone
    update(this)
}

fun WordBean.errorCountAdd() {
    this.errorCount = this.errorCount + 1
    update(this)
}

fun WordBean.errorCountClear() {
    this.errorCount = 0
    update(this)
}

fun update(bean: WordBean) {
    GlobalScope.launch {
        App.getDb().word().update(bean)
    }
}

fun WordBean.getLineInterpret(): String {

    return interpret.replace("；n.", "；\nn.")
        .replace("；num.", "；\nnum.")
        .replace("；prep.", "；\nprep.")
        .replace("；adv.", "；\nadv.")
        .replace("；adj.", "；\nadj.")
        .replace("；aux.", "；\naux.")
        .replace("；conj.", "；\nconj.")
        .replace("；v.", "；\nv.")
        .replace("；vt.", "；\nvt.")
        .replace("；vi.", "；\nvi.")
}

fun WordBean.getExample(): String {
    val ret: MutableList<String> = arrayListOf()

    val list = example.split("||||").toMutableList()
    list.removeAt(0)
    list.forEach {
        val index = findChineseIndex(it)
        ret.add(it.substring(0, index))
        ret.add(it.substring(index, it.length))
    }
    return ret.joinToString("\n")
}

fun findChineseIndex(text: String): Int {
    return text.indexOfFirst {
        it.code > 126
    }
}

fun WordBean.getAudioUrl(): String {
    return "https://dict.youdao.com/dictvoice?audio=${name}"
}