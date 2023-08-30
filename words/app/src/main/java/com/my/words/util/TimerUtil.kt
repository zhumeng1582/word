package com.my.words.util

import com.blankj.utilcode.util.TimeUtils
import java.util.TimeZone

object TimerUtil {
    var dayMills = 24 * 60 * 60 * 1000
    //获取当天0点0分0秒的时间戳
    fun getTodayMills():Long{
        val time = TimeUtils.getNowMills()
        return time-(time + TimeZone.getDefault().rawOffset) % dayMills
    }
}