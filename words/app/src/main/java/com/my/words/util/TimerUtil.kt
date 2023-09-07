package com.my.words.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar
import java.util.TimeZone

object TimerUtil {
    val TAG = "TimerUtil"

    var dayMills = 24 * 60 * 60 * 1000

    //获取当天0点0分0秒的时间戳
    fun getTodayMills(): Long {
        val time = TimeUtils.getNowMills()
        return getDayStartMills(time)
    }

    private fun getDayStartMills(time: Long): Long {
        return time - (time + TimeZone.getDefault().rawOffset) % dayMills
    }

    fun getMonthStartAndEnd(year: Int, month: Int): Pair<Long, Long> {
        val calendar: Calendar = Calendar.getInstance()
        Log.d(TAG,"------>year = "+year+",month = "+month)

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month-1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val start = getDayStartMills(calendar.timeInMillis)

        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)

        val end = getDayStartMills(calendar.timeInMillis)
        Log.d(TAG,"------>start = "+start+",end = "+end)

        return Pair(start, end)
    }

    fun getFirstDayOfWeek(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar[year, month - 1] = 1 // Month value is 0-based. So, you need to subtract 1.
        return calendar[Calendar.DAY_OF_WEEK]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun timestampToLocalDate(date: LocalDate): Long {
        return date.atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli()
    }
}