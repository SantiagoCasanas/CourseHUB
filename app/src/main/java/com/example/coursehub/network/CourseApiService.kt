package com.example.coursehub.network


import android.util.Log
import com.example.coursehub.users.LoginResponse
import com.example.coursehub.users.LoginUser
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.coursehub.users.GetTokenData
import com.example.coursehub.users.ResetPassData
import com.example.coursehub.users.TokenRespose
import com.example.coursehub.users.UserCreateResponse
import com.example.coursehub.users.UserInfo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import java.io.File
import javax.inject.Inject



private const val BASE_URL = "https://jesus.pythonanywhere.com/"
const val PREFS_NAME = "CourseHUB_prefs"


private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val  userService = retrofit.create(UserService::class.java)
class Auth{
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
            Log.d("User info:", "${service.userRetrieveInfo()}")
            service.userRetrieveInfo()
        }catch (e: Exception){
            Log.d("Unauthorized:", "${e.message}")
            null
        }
    }

    suspend fun update_info(email: String?,
                            fullName: String?,
                            username: String?,
                            pictureUri: File?
    ): Result<UserCreateResponse> {
        return try {
            val service = providesAuth().create(UserService::class.java)
            val requestBody = pictureUri!!.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("profile", pictureUri!!.name, requestBody)
            val usernameBody = username!!.toRequestBody("text/plain".toMediaTypeOrNull())
            val emailBody = email!!.toRequestBody("text/plain".toMediaTypeOrNull())
            val fullNameBody = fullName!!.toRequestBody("text/plain".toMediaTypeOrNull())

            Log.d("Image:", "${part}")
            val response = service.updateOwnInfo(usernameBody, emailBody, fullNameBody, part)
            if (response!= null) {
                Result.success(response)
            } else {
                Result.failure(Exception("Error: User object not found in the response"))
            }
        } catch (e: Exception) {
            Log.d("Create error:", "${e.message}")
            Result.failure(e)
        }
    }

}

interface UserService{
    @Multipart
    @POST("user/create")
    suspend fun createUser(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("full_name") fullName: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part?
    ): UserCreateResponse

    @POST("user/login")
    suspend fun loginUser(@Body loginRequest: LoginUser): LoginResponse

    @GET("user/own_info")
    suspend fun userRetrieveInfo(): UserInfo

    @Multipart
    @PUT("user/update_own_info")
    suspend fun updateOwnInfo(
        @Part("username") username: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("full_name") fullName: RequestBody?,
        @Part image: MultipartBody.Part?
    ): UserCreateResponse

    @POST("password/get-token-reset-password")
    suspend fun sendToken(@Body email: GetTokenData): TokenRespose

    @POST("password/reset")
    suspend fun resetPass(@Body data: ResetPassData ): TokenRespose
}


suspend fun sendCreateUserData(
    email: String,
    fullName: String,
    username: String,
    password: String,
    pictureUri: File?,
    create:()-> Unit
): Result<UserCreateResponse> {
    return try {
        val requestBody = pictureUri!!.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("profile", pictureUri!!.name, requestBody)
        val usernameBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val fullNameBody = fullName.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordBody = password.toRequestBody("text/plain".toMediaTypeOrNull())

        Log.d("Image:", "${part}")
        val response = userService.createUser(usernameBody, emailBody, fullNameBody, passwordBody, part)
        if (response != null) {
            create()
            Result.success(response)
        } else {
            Result.failure(Exception("Error: User object not found in the response"))
        }
    } catch (e: Exception) {
        Log.d("Create error:", "${e.message}")
        Result.failure(e)
    }
}

suspend fun sendRecoverCode(email: String, resetPass:()-> Unit){
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val data = GetTokenData(email)
            val response = userService.sendToken(data)
            if (response.detail!=null){
                if (response.detail == "the token has been sent."){
                    withContext(Dispatchers.Main) {
                        resetPass()
                    }
                    Log.d("Detail:","${response.detail}")
                }else{
                    Log.d("Detail:","${response.detail}")
                }
            }else{
                Log.d("Error:","${response.error}")
            }
        }catch (e:Exception){
            withContext(Dispatchers.Main) {
                Log.d("Token error:","${e.message}")
            }
        }
    }
}

suspend fun resetPassword(email: String,token: String,password: String, resetPass:()-> Unit){
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val data = ResetPassData(email,token, password)
            val response = userService.resetPass(data)
            if (response.detail!=null){
                if (response.detail == "Password has been updated."){
                    withContext(Dispatchers.Main) {
                        Log.d("Detail:","${response.detail}")
                        resetPass()
                    }
                    response
                }else{
                    Log.d("Detail:","${response.detail}")
                    response
                }
            }else{
                Log.d("Error:","${response.error}")
                response
            }
        }catch (e:Exception){
            Log.d("Reset pass error:","${e.message}")
            null
        }
    }
}