package com.example.coursehub.network


import android.content.Context
import android.util.Log
import android.widget.Toast
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
import com.example.coursehub.users.UserInfoResponse
import okhttp3.MediaType
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

object ErrorInterceptorProvider {
    val interceptor = ErrorInterceptor() // Instancia única del ErrorInterceptor
}
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(ErrorInterceptorProvider.interceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val  userService = retrofit.create(UserService::class.java)
class Auth{
    @Inject
    lateinit var tokenManager: TokenManager

    suspend fun sendLoginUserData( context: Context,username: String, password: String, home:()-> Unit){
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
            }catch (e:Exception){
                withContext(Dispatchers.Main) {
                    val errorToShow = if (ErrorInterceptorProvider.interceptor.lastError != null) {
                        ErrorInterceptorProvider.interceptor.lastError
                    } else {
                        e.message
                    }
                    Toast.makeText(context, errorToShow, Toast.LENGTH_SHORT).show()
                    Log.d("Token error:","${errorToShow}")
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
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), pictureUri!!)
            val part = MultipartBody.Part.createFormData("profile_picture", pictureUri!!.name, requestBody)
            val emailBody = email?.toRequestBody("text/plain".toMediaTypeOrNull())
            val fullNameBody = fullName?.toRequestBody("text/plain".toMediaTypeOrNull())
            val usernameBody = username?.toRequestBody("text/plain".toMediaTypeOrNull())
            Log.d("Data user:", "${requestBody},${emailBody},${fullNameBody},${usernameBody}")
            val response = service.updateOwnInfo(emailBody, fullNameBody, usernameBody, part)
            Log.d("update data:","${response}")
            if (response != null) {
                Log.d("Updated:","${response.email}")
                Result.success(response)
            } else {
                Result.failure(Exception("Error: User object not found in the response"))
            }
        } catch (e: Exception) {
            Log.d("Update error:", "${e.message}")
            Result.failure(e)
        }
    }

    suspend fun updateuser(email: String?, fullName: String?, username: String?){
        try {
            val service = providesAuth().create(UserService::class.java)
            val data = UserInfoResponse(email, fullName, username)
            val response = service.updateInfo(data)
            Log.d("Updated:", "${response}")
        } catch (e: Exception) {
            Log.d("Error updating:", "${e.message}")
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
        @Part("email") email: RequestBody?,
        @Part("full_name") fullName: RequestBody?,
        @Part("username") username: RequestBody?,
        @Part image: MultipartBody.Part?
    ): UserCreateResponse

    @PUT("user/update_own_info")
    suspend fun updateInfo(@Body data: UserInfoResponse): UserCreateResponse

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
    pictureUri: File?
    //create:()-> Unit
): Result<UserCreateResponse> {
    return try {
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), pictureUri!!)
        val part = MultipartBody.Part.createFormData("profile_picture", pictureUri!!.name, requestBody)
        val usernameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), username)
        val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
        val fullNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), fullName)
        val passwordBody = RequestBody.create("text/plain".toMediaTypeOrNull(), password)
        val response = userService.createUser(usernameBody, emailBody, fullNameBody, passwordBody, part)
        if (response != null) {
            //create()
            Log.d("Created:","${response.email}")
            Result.success(response)
        } else {
            Result.failure(Exception("Error: User object not found in the response"))
        }
    } catch (e: Exception) {
        Log.d("Create error:", "${e.message}")
        Result.failure(e)
    }
}

suspend fun sendRecoverCode(context: Context, email: String, resetPass:()-> Unit){
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val data = GetTokenData(email)
            val response = userService.sendToken(data)
            Log.d("Error:","${response}")
            withContext(Dispatchers.Main) {
                if (response.detail != null) {
                    if (response.detail == "the token has been sent.") {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, response.detail, Toast.LENGTH_SHORT).show()
                            resetPass()
                        }
                        Log.d("Detail:", "${response.detail}")
                    } else {
                        Toast.makeText(context, response.detail, Toast.LENGTH_SHORT).show()
                        Log.d("Detail:", "${response.detail}")
                    }
                }
            }
        }catch (e: Exception){
            withContext(Dispatchers.Main) {
                val errorToShow = if (ErrorInterceptorProvider.interceptor.lastError != null) {
                    ErrorInterceptorProvider.interceptor.lastError
                } else {
                    e.message
                }
                Toast.makeText(context, errorToShow, Toast.LENGTH_SHORT).show()
                Log.d("Token error:","${errorToShow}")
            }
        }
    }
}

suspend fun resetPassword(context: Context,email: String,token: String,password: String, resetPass:()-> Unit){
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val data = ResetPassData(email,token, password)
            val response = userService.resetPass(data)
            withContext(Dispatchers.Main) {
                if (response.detail!=null){
                    if (response.detail == "Password has been updated."){
                        Toast.makeText(context, response.detail, Toast.LENGTH_SHORT).show()
                        Log.d("Detail:","${response.detail}")
                        resetPass()
                    }else{
                        Toast.makeText(context, response.detail, Toast.LENGTH_SHORT).show()
                        Log.d("Detail:","${response.detail}")
                    }
                }
            }
        }catch (e:Exception){
            withContext(Dispatchers.Main) {
                val errorToShow = if (ErrorInterceptorProvider.interceptor.lastError != null) {
                    ErrorInterceptorProvider.interceptor.lastError
                } else {
                    e.message
                }
                Toast.makeText(context, errorToShow, Toast.LENGTH_SHORT).show()
                Log.d("Token error:","${errorToShow}")
            }
        }
    }
}