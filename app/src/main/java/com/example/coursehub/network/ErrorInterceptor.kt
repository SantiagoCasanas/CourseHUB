package com.example.coursehub.network

import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            // Aquí capturas la respuesta errónea y puedes manejarla como desees
            val errorBody = response.body?.string()
            // Loguear o manejar el error de alguna manera
        }

        return response
    }
}