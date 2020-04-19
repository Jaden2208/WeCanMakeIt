package com.whalez.wecanmakeit.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    var timestamp: Long,
//    var content: ArrayList<String>,
    var content: String
//    var isDone: ArrayList<String>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}