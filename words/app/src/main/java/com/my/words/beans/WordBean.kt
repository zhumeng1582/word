package com.my.words.beans
data class WordBean(
    val name: String,
    val phonetic: String,
    val sound: String,
    val interpret: String,
)

fun WordBean.getLineInterpret():String{
    return interpret.replace(";","\n").replace("；","\n")
}