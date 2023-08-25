package com.my.words.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.my.words.beans.LearnRecord
import com.my.words.beans.WordBean

@Dao
interface LearnRecordDao {
    @Query("SELECT * from WordBean INNER JOIN LearnRecord ON WordBean.id=LearnRecord.wordId")
    fun queryAll(): List<WordBean>

    @Insert
    fun insert(vararg record: LearnRecord): List<Long>

    @Update
    fun update(record: LearnRecord): Int

    @Query("SELECT count(*) from LearnRecord where LearnRecord.wordId = :wordId and LearnRecord.time = :time")
    fun exit(wordId: Int, time: Long): Long

}