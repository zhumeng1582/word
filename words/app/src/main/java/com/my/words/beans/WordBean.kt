package com.my.words.beans

data class WordBean(
    val name: String,
    val phonetic: String,
    val sound: String,
    val interpret: String,
    var youDaoWord: YouDaoWord?
)

fun WordBean.getLineInterpret(): String {
    return interpret.replace(";", "\n").replace("；", "\n")
}

fun WordBean.getAudioUrl(): String {
    return "https://dict.youdao.com/dictvoice?audio=${name}"
}