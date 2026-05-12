package com.mindmatrix.nammakathey.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val mobile: String,
    val email: String,
    val passwordHash: String,
    val quizzesAttended: Int = 0,
    val quizScore: Int = 0,
    val storiesRead: Int = 0
)
