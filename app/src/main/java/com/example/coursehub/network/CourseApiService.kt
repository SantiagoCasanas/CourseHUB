package com.example.coursehub.network

import android.util.Log
import com.example.coursehub.users.LoginResponse
import com.example.coursehub.users.LoginUser
import com.example.coursehub.users.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val BASE_URL = "https://jesus.pythonanywhere.com/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val  userService = retrofit.create(UserService::class.java)

interface UserService{
    @POST("user/create/")
    suspend fun createUser(@Body user: User): User

    @POST("user/login/")
    suspend fun loginUser(@Body loginRequest: LoginUser): LoginResponse
}

suspend fun sendCreateUserData(email:String,fullName:String,username:String,password:String){
    try {
        val user = User(username, email, fullName, password)
        val response = userService.createUser(user)
        Log.d("User created:", "${response.email}")
    } catch (e: Exception) {
        Log.d("Signup error:","${e.message}")
    }
}

suspend fun sendLoginUserData(username: String, password: String, home:()-> Unit){
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val loginUser = LoginUser(username, password)
            val response = userService.loginUser(loginUser)
            withContext(Dispatchers.Main) {
                Log.d("you're logged in", "${response.access}")
                home()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d("Connection error", "${e.message}")
            }
        }
    }
}



