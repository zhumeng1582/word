package com.my.words.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.my.words.beans.WordBean
import com.my.words.util.CacheUtil

@Dao
interface WordDao {
    @Query("select count(*) from WordBean")
    fun querySize(): Long

    @Query("select * from WordBean")
    fun queryAll(): List<WordBean>

    @Query("select * from WordBean where level = :level")
    fun query(level: Int): List<WordBean>

    @Query("select * from WordBean where errorCount > 0")
    fun queryAllError(): List<WordBean>

    @Query("select * from WordBean where isDone = true")
    fun queryAllDone(): List<WordBean>

    @Query("select * from  WordBean where WordBean.id>:learnId and WordBean.level = :level limit :count")
    fun queryAllNotLearn(
        level: Int,
        learnId: Int,
        count: Int = CacheUtil.getLearnWordCount()
    ): List<WordBean>

    @Query("select count(*) from  WordBean where WordBean.id>:learnId and WordBean.level = :level")
    fun unlearnedCount(level: Int, learnId:Int =  CacheUtil.getLearnWordId()): Long

    @Query("select * from  WordBean ORDER BY RANDOM() limit 4 ")
    fun queryTestData(): List<WordBean>

    @Insert
    fun insert(vararg wordBean: WordBean): List<Long>

    @Insert
    fun insert(wordBeanList: List<WordBean>): List<Long>

    @Delete
    fun delete(wordBean: WordBean): Int

    @Update
    fun update(wordBean: WordBean): Int

}