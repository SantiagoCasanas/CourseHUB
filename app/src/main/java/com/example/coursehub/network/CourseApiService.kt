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
import androidx.compose.ui.platform.LocalContext
import com.example.coursehub.users.UserInfo
import okhttp3.OkHttpClient
import retrofit2.create
import retrofit2.http.GET
import javax.inject.Inject

private const val BASE_URL = "https://jesus.pythonanywhere.com/"
const val PREFS_NAME = "CourseHUB_prefs"


private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val  userService = retrofit.create(UserService::class.java)
class Login{
    @Inject
    lateinit var tokenManager: TokenManager

    suspend fun sendLoginUserData( username: String, password: String, home:()-> Unit){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val loginUser = LoginUser(username, password)
                val response = userService.loginUser(loginUser)
                withContext(Dispatchers.Main) {
                    Log.d("you're logged in", "${response.access}")
                    tokenManager.saveAccessToken(response.access)
                    Log.d("Token saved","${tokenManager.getAccessToken()}")
                    home()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("Connection error", "${e.message}")
                }
            }
        }
    }

    private fun providesAuth(): Retrofit{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager.getAccessToken() ?: ""))
            .build()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    suspend fun getUserInfo(): UserInfo? {
        val service = providesAuth().create(UserService::class.java)
        return try {
            Log.d("user:", "${service.userRetrieveInfo().toString()}")
            service.userRetrieveInfo()
        }catch (e: Exception){
            Log.d("Unauthorized:", "${e.message}")
            null
        }
    }

}

interface UserService{
    @POST("user/create")
    suspend fun createUser(@Body user: User): User

    @POST("user/login")
    suspend fun loginUser(@Body loginRequest: LoginUser): LoginResponse

    @GET("user/own_info")
    suspend fun userRetrieveInfo(): UserInfo
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



