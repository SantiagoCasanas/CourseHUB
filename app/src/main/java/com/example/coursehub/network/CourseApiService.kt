package com.example.coursehub.network

import android.content.Context
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
import android.content.SharedPreferences

private const val BASE_URL = "https://jesus.pythonanywhere.com/"
private const val PREFS_NAME = "CourseHUB_prefs"

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

suspend fun sendCreateUserData(email:String,fullName:String,username:String,password:String):Result<User> {
    return try {
        val user = User(username, email, fullName, password)
        val response = userService.createUser(user)
        //Log.d("User created:", "${response.username}")
        Result.success(response)
    } catch (e: Exception) {
        //Log.d("Signup error:","${e.message}")
        Result.failure(e)
    }
}

suspend fun sendLoginUserData(context: Context, username: String, password: String, home:()-> Unit){
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val loginUser = LoginUser(username, password)
            val response = userService.loginUser(loginUser)
            withContext(Dispatchers.Main) {
                Log.d("you're logged in", "${response.access}")
                saveAccessToken(context,response.access)
                Log.d("Token saved","${getAccessToken(context)}")
                home()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d("Connection error", "${e.message}")
            }
        }
    }
}

private fun saveAccessToken(context: Context, accessToken: String) {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("access_token", accessToken)
    editor.apply()
}

private fun getAccessToken(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString("access_token", null)
}

private fun saveRefreshToken(context: Context, refreshToken: String) {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("refresh_token", refreshToken)
    editor.apply()
}

private fun getRefreshToken(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString("refresh_token", null)
}



