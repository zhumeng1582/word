package com.my.words.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.my.words.beans.LearnRecord
import com.my.words.beans.WordBean

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

    @Query("select distinct LearnRecord.time from LearnRecord ORDER BY LearnRecord.time")
    fun accumulateContinueAccount(): List<LearnRecord>

    @Query("SELECT count(distinct LearnRecord.time) from LearnRecord")
    fun accumulateAccount(): Int

    @Query("SELECT count(distinct LearnRecord.wordId) from LearnRecord")
    fun accumulateLearnWords(): Int

    @Query("SELECT count(*) from LearnRecord where LearnRecord.wordId = :wordId and LearnRecord.time = :time")
    fun exit(wordId: Int, time: Long): Long

}