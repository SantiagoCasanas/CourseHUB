package com.example.coursehub.network

import android.content.Context
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext

class TokenManager @Inject constructor(@ApplicationContext context: Context){
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveAccessToken(accessToken: String) {
        val editor = prefs.edit()
        editor.putString("access_token", accessToken)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    fun saveRefreshToken(context: Context, refreshToken: String) {
        val editor = prefs.edit()
        editor.putString("refresh_token", refreshToken)
        editor.apply()
    }

    fun getRefreshToken(context: Context): String? {
        return prefs.getString("refresh_token", null)
    }
}