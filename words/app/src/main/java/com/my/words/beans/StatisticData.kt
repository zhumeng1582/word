package com.my.words.beans

data class StatisticData(
    var accumulateAccount: Int = 0,
    var accumulateLearntAccount: Int = 0,
    var maxContinueAccount: Int = 0,
    var todayLearnAccount: Int = 0,
    var unlearnedCount: Long = 0
)
