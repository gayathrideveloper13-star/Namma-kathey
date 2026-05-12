package com.mindmatrix.nammakathey.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("nammakathey_prefs", Context.MODE_PRIVATE)

    fun loginUser(userId: Long) {
        prefs.edit().putLong("USER_ID", userId).apply()
    }

    fun logoutUser() {
        prefs.edit().remove("USER_ID").apply()
    }

    fun getLoggedInUserId(): Long {
        return prefs.getLong("USER_ID", -1L)
    }

    fun isLoggedIn(): Boolean {
        return getLoggedInUserId() != -1L
    }
}
