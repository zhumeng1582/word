package com.my.words.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.my.words.beans.WordBean

@Database(entities = [WordBean::class], version = 3, exportSchema = true)
abstract class SQLDatabase : RoomDatabase() {
    abstract fun word(): WordDao
}