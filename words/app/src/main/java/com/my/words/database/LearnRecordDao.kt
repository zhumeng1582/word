package com.my.words.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.my.words.beans.LearnRecord
import com.my.words.beans.WordBean
import com.my.words.util.TimerUtil

@Dao
interface LearnRecordDao {
    @Query("SELECT * from WordBean INNER JOIN LearnRecord ON WordBean.id=LearnRecord.wordId")
    fun queryAllLearn(): List<WordBean>

    @Query("SELECT * from WordBean INNER JOIN (select distinct LearnRecord.wordId from LearnRecord) relation ON WordBean.id=relation.wordId")
    fun queryAllLearnDistinct(): List<WordBean>

    @Insert
    fun insert(vararg record: LearnRecord): List<Long>

    @Update
    fun update(record: LearnRecord): Int


    //学习日期排序
    @Query("select distinct LearnRecord.time from LearnRecord ORDER BY LearnRecord.time")
    fun getAllDayRecord(): List<LearnRecord>

    //学习日期排序
    @Query("select distinct LearnRecord.time from LearnRecord where LearnRecord.time>= :start and LearnRecord.time<=:end ORDER BY LearnRecord.time")
    fun getPeriodRecord(start: Long, end: Long): List<LearnRecord>

    fun getMonthRecord(year: Int, month: Int): List<LearnRecord> {
        val period = TimerUtil.getNextMonth(year,month)
        return getPeriodRecord(period.first, period.second)
    }

    //累计学习天数
    @Query("SELECT count(distinct LearnRecord.time) from LearnRecord")
    fun accumulateAccount(): Int

    //累计学习单词数
    @Query("SELECT count(distinct LearnRecord.wordId) from LearnRecord")
    fun accumulateLearnWords(): Int

    //累计学习单词数
    @Query("SELECT count(distinct LearnRecord.wordId) from LearnRecord where LearnRecord.time =:time")
    fun todayLearnWords(time: Long): Int

    @Query("SELECT count(*) from LearnRecord where LearnRecord.wordId = :wordId and LearnRecord.time = :time")
    fun exit(wordId: Int, time: Long): Long

}