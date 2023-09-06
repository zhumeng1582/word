package com.my.words.util

import com.blankj.utilcode.util.TimeUtils
import java.util.Calendar
import java.util.TimeZone

object TimerUtil {
    var dayMills = 24 * 60 * 60 * 1000

    //获取当天0点0分0秒的时间戳
    fun getTodayMills(): Long {
        val time = TimeUtils.getNowMills()
        return getDayStartMills(time)
    }
    private fun getDayStartMills(time:Long): Long {
        return time - (time + TimeZone.getDefault().rawOffset) % dayMills
    }

    fun getNextMonth(year: Int, month: Int): Pair<Long,Long> {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.add(Calendar.MONTH, month-1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val start = getDayStartMills(calendar.timeInMillis)

        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)

        val end = getDayStartMills(calendar.timeInMillis)

        return Pair(start,end)
    }
}