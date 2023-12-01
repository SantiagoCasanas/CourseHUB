package com.example.coursehub.network

import android.util.Log
import com.example.coursehub.users.TokenRespose
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor : Interceptor {
    var lastError: String? = null
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            val errorBody = response.body?.string()
            val error = Gson().fromJson(errorBody, TokenRespose::class.java)
            lastError = error.error ?: error.detail ?:error.profile_picture
            // Imprimir el error
            Log.d("ServerError", "Error: ${lastError}")
        }

        return response
    }
}