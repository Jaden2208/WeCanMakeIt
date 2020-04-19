package com.whalez.wecanmakeit.ui.main.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.whalez.wecanmakeit.room.TodoDatabase
import com.whalez.wecanmakeit.room.data.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application): AndroidViewModel(application) {
    // 앱 데이터베이스 생성
    private val db = Room.databaseBuilder(
        application,
        TodoDatabase::class.java, "todo-db"
    ).build()

    fun insert(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            db.todoDao().insert(todo)
        }
    }

    fun update(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            db.todoDao().update(todo)
        }
    }

    fun delete(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            db.todoDao().delete(todo)
        }
    }

    fun getAll(): LiveData<List<Todo>> {
        return db.todoDao().getAllTodo()
    }

    fun getTodaysTodo(timestamp: Long): LiveData<List<Todo>>{
        return db.todoDao().getTodaysTodo(timestamp)
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            db.todoDao().deleteAllTodo()
        }
    }

    fun deleteSelected(idList: List<Int>){
        viewModelScope.launch(Dispatchers.IO) {
            db.todoDao().deleteSelectedTodo(idList)
        }
    }

}