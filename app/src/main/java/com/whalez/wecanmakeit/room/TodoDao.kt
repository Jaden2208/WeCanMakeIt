package com.whalez.wecanmakeit.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.whalez.wecanmakeit.room.data.Todo

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("select * from Todo order by timestamp desc")
    fun getAllTodo(): LiveData<List<Todo>>

    @Query("select * from Todo where timestamp=(:timestamp)")
    fun getTodaysTodo(timestamp: Long): LiveData<List<Todo>>

    @Query("delete from Todo")
    fun deleteAllTodo()

    @Query("delete from Todo where id in (:idList)")
    fun deleteSelectedTodo(idList: List<Int>)
}