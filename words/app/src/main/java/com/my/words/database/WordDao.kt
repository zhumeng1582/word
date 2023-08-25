package com.my.words.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.my.words.beans.WordBean

@Dao
interface WordDao {
    @Query("select count(*) from WordBean")
    fun querySize(): Long
    @Query("select * from WordBean")
    fun queryAll(): List<WordBean>
    @Query("select * from WordBean where level = :level")
    fun query(level: Int): List<WordBean>

    @Insert
    fun insert(vararg book: WordBean): List<Long>

    @Insert
    fun insert(wordList: List<WordBean>): List<Long>

    @Delete
    fun delete(book: WordBean): Int

    @Update
    fun update(book: WordBean): Int

}