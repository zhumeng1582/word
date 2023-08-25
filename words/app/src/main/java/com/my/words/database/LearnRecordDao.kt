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
    fun insert(vararg wordBean: LearnRecord): List<Long>

}