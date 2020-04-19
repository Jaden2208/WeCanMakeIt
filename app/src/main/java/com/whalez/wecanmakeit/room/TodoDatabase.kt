package com.whalez.wecanmakeit.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.whalez.wecanmakeit.room.data.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}