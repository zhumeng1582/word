package com.my.words.beans

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.my.words.App
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Entity(
    primaryKeys = ["wordId", "time"],
    foreignKeys = [ForeignKey(
        entity = WordBean::class,
        childColumns = ["wordId"], // tab_teacher的列名
        parentColumns = ["id"] // 关联的tab_course表的主键列名
    )], indices = [Index("wordId")]
)
data class LearnRecord(
    val wordId: Int,
    val time: Long,
)

@OptIn(DelicateCoroutinesApi::class)
fun addRecord(bean: LearnRecord) {
    GlobalScope.launch {
        if (App.getDb().record().exit(bean.wordId, bean.time) == 0L) {
            App.getDb().record().insert(bean)
        } else {
            App.getDb().record().update(bean)
        }
    }
}