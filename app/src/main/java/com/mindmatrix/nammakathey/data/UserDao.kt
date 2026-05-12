package com.mindmatrix.nammakathey.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE mobile = :identifier OR email = :identifier LIMIT 1")
    suspend fun getUserByEmailOrMobile(identifier: String): User?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    @Query("UPDATE users SET storiesRead = storiesRead + 1 WHERE id = :userId")
    suspend fun incrementStoriesRead(userId: Long)

    @Query("UPDATE users SET quizzesAttended = quizzesAttended + 1, quizScore = quizScore + :scoreToAdd WHERE id = :userId")
    suspend fun updateQuizStats(userId: Long, scoreToAdd: Int)
}
