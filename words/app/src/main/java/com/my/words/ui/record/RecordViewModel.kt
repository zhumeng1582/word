package com.my.words.ui.record

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils
import com.my.words.App
import com.my.words.beans.LearnRecord
import com.my.words.util.TimerUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

data class Record(val localDate: LocalDate, val punched: Boolean)

@RequiresApi(Build.VERSION_CODES.O)
class RecordViewModel : ViewModel() {
    val TAG = "RecordViewModel"

    init {
        getDaysOfMonthWithBlanks(YearMonth.now())
    }

    val recordList: MutableLiveData<List<Record?>> = MutableLiveData()

    @RequiresApi(Build.VERSION_CODES.O)
    val currentMonth: MutableLiveData<YearMonth> = MutableLiveData(YearMonth.now())

    @RequiresApi(Build.VERSION_CODES.O)
    fun minusMonths() {
        currentMonth.value = currentMonth.value?.minusMonths(1)
        getDaysOfMonthWithBlanks(currentMonth.value!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun plusMonths() {
        currentMonth.value = currentMonth.value?.plusMonths(1)
        getDaysOfMonthWithBlanks(currentMonth.value!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDaysOfMonthWithBlanks(currentMonth: YearMonth = YearMonth.now()) {

        val year = currentMonth.year
        val month = currentMonth.monthValue
        GlobalScope.launch {
            val blankDays = TimerUtil.getFirstDayOfWeek(year, month) % 7
            val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
            val recordLearnList = getMonthRecord(year, month)
            LogUtils.d(TAG, recordLearnList.size)

            val list = List(blankDays) { null } + (1..daysInMonth).map { it1 ->
                val day = LocalDate.of(year, month, it1)
                val timestamp = TimerUtil.timestampToLocalDate(day)
                val punched = recordLearnList.count { timestamp == it.time } > 0

                Record(day, punched)
            }

            recordList.postValue(list)
        }

    }


    private fun getMonthRecord(year: Int, month: Int): List<LearnRecord> {
        val period = TimerUtil.getMonthStartAndEnd(year, month)
        return App.getDb().record().getPeriodRecord(period.first, period.second)
    }
}