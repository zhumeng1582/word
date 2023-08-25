package com.my.words.beans

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.my.words.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Entity(
    foreignKeys = [ForeignKey(
        entity = WordBean::class,
        childColumns = ["wordId"], // tab_teacher的列名
        parentColumns = ["id"] // 关联的tab_course表的主键列名
    )], indices = [Index("wordId")]
)
data class LearnRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val wordId: Int,
    val time: Long,
)

fun addRecord(bean: LearnRecord) {
    GlobalScope.launch {
        App.getDb().record().insert(bean)
    }
}