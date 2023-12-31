package com.example.coursehub.network


import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.coursehub.R
import com.example.coursehub.users.CourseData
import com.example.coursehub.users.CreateChapterData
import com.example.coursehub.users.CreateCourseData
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
import com.example.coursehub.users.LogoutData
import com.example.coursehub.users.LogoutResponse
import com.example.coursehub.users.MyCourseData
import com.example.coursehub.users.ResetPassData
import com.example.coursehub.users.ResponseChapterData
import com.example.coursehub.users.TakeCourseData
import com.example.coursehub.users.TokenRespose
import com.example.coursehub.users.TopicData
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
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File
import java.net.ProtocolException
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
                    tokenManager.saveRefreshToken(context,response.refresh)
                    Log.d("Token access:","${tokenManager.getAccessToken()}")
                    Log.d("Token refresh:","${tokenManager.getRefreshToken(context)}")
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
                            pictureUri: File?,
                            context: Context
    ){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
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
                        Toast.makeText(context, R.string.updated_user, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, R.string.error_update, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("Update error:", "${e.message}")
                    Toast.makeText(context, R.string.error_update, Toast.LENGTH_SHORT).show()
                }
            }
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

    suspend fun userLogout(tokenRefresh: String?, login:()->Unit){
        GlobalScope.launch(Dispatchers.IO) {
            val service = providesAuth().create(UserService::class.java)
            try {
                val token = LogoutData(tokenRefresh!!)
                Log.d("Token:", "${tokenRefresh}")
                val response = service.logout(token)
                withContext(Dispatchers.Main) {
                    if (response.detail=="successful logout"){
                        login()
                        Log.d("Logout :", "${token.refresh}")
                        Log.d("Logout :", "${response.detail}")
                    }else{
                        Log.d("Error:","${response.code}")
                    }
                }
            } catch (e: ProtocolException) {
                withContext(Dispatchers.Main) {
                    if (e.message?.contains("HTTP 205") == true) {
                        login()
                        Log.d("Success:", ":D")
                    } else {
                        Log.d("Error logout:", "${e.message}")

                    }
                }
            } catch (e:Exception){
                withContext(Dispatchers.Main) {
                    Log.d("Error logout:", "${e.message}")
                }
            }
        }
    }

    suspend fun deactivateUser(context: Context,login:()->Unit){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    val service = providesAuth().create(UserService::class.java)
                    val response = service.deactivateAccount()
                    Toast.makeText(context, response.detail, Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main) {
                    if (e.message?.contains("HTTP 401") == true) {
                        login()
                        Log.d("Success:", ":D")
                        Toast.makeText(context,"Account deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("Error logout:", "${e.message}")

                    }
                }
            }
        }
    }

    suspend fun createNewCourse(
        title: String,
        description: String,
        topic: String,
        course:()->Unit
    ){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){
                    val service = providesAuth().create(UserService::class.java)
                    val data = CreateCourseData(title,description,topic)
                    val response = service.createCourse(data)
                    Log.d("Course created","$response")
                    course()
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Log.d("Course error:","Failed to create ${e.message}")
                }
            }
        }
    }

    suspend fun retrieveTopics():List<TopicData>?{
        return try {
            val service = providesAuth().create(UserService::class.java)
            val response = service.getTopics()
            Log.d("Topics:","$response")
            response
        }catch (e: Exception){
            Log.d("Topics error:","Failed to list ${e.message}")
            null
        }
    }

    suspend fun retrieveCourses():List<CourseData>?{
        return try {
            val service = providesAuth().create(UserService::class.java)
            val response = service.getCourses()
            Log.d("Courses:","$response")
            response
        }catch (e: Exception){
            Log.d("Courses error:","Failed to list ${e.message}")
            null
        }
    }

    suspend fun retrieveCoursesCreatedByMe():List<CourseData>?{
        return try {
            val service = providesAuth().create(UserService::class.java)
            val response = service.getCoursesCreatedByMe()
            Log.d("My courses:","$response")
            response
        }catch (e: Exception){
            Log.d("Courses error:","Failed to list ${e.message}")
            null
        }
    }

    suspend fun retrieveCoursesIHaveTaken():List<MyCourseData>?{
        return try {
            val service = providesAuth().create(UserService::class.java)
            val response = service.getCoursesIHaveTaken()
            Log.d("My courses:","$response")
            response
        }catch (e: Exception){
            Log.d("Courses error:","Failed to list ${e.message}")
            null
        }
    }

    suspend fun createNewChapter(
        courseId: String,
        title: String,
        description: String,
        course:()->Unit
    ){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){
                    val service = providesAuth().create(UserService::class.java)
                    val data = CreateChapterData(courseId,title,description)
                    val response = service.createChapter(data)
                    Log.d("Chapter created","$response")
                    course()
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Log.d("Chapter error:","Failed to create ${e.message}")
                }
            }
        }
    }

    suspend fun retrieveCourse(id:String):CourseData?{
        return try {
            val service = providesAuth().create(UserService::class.java)
            val response = service.getCourseById(id.toInt())
            Log.d("Courses:","$response")
            response
        }catch (e: Exception){
            Log.d("Courses error:","Failed to retrieve ${id.toInt().javaClass} ${e.message}")
            null
        }
    }

    suspend fun takeNewCourse(
        courseId: String,
        course:()->Unit
    ){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){
                    val service = providesAuth().create(UserService::class.java)
                    val data = TakeCourseData(courseId)
                    val response = service.takeCourse(data)
                    Log.d("Course started","$response")
                    course()
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Log.d("Course take error:","Failed ${e.message}")
                }
            }
        }
    }

    suspend fun retrieveChapters(id:String):List<ResponseChapterData>?{
        return try {
            val service = providesAuth().create(UserService::class.java)
            val response = service.getChaptersById(id.toInt())
            Log.d("Courses:","$response")
            response
        }catch (e: Exception){
            Log.d("Courses error:","Failed to retrieve ${id.toInt().javaClass} ${e.message}")
            null
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

    @POST("user/logout")
    suspend fun logout(@Body data: LogoutData ): LogoutResponse

    @PUT("user/deactivate_account")
    suspend fun deactivateAccount(): TokenRespose

    @GET("course/list_topics/")
    suspend fun getTopics(): List<TopicData>

    @GET("course/create_list_course/")
    suspend fun getCourses(): List<CourseData>

    @POST("course/create_list_course/")
    suspend fun createCourse(@Body data: CreateCourseData): CourseData

    @GET("course/list_my_courses/")
    suspend fun getCoursesIHaveTaken(): List<MyCourseData>?

    @GET("course/list_courses_created_by_me/")
    suspend fun getCoursesCreatedByMe(): List<CourseData>?

    @POST("course/create_chapter/")
    suspend fun createChapter(@Body data: CreateChapterData): ResponseChapterData

    @GET("course/course/{id}")
    suspend fun getCourseById(@Path("id") courseId: Int): CourseData?

    @POST("course/user_take_course/")
    suspend fun takeCourse(@Body data: TakeCourseData): MyCourseData

    @GET("course/course/{course_id}/chapters/")
    suspend fun getChaptersById(@Path("course_id") courseId: Int): List<ResponseChapterData>?
}


suspend fun sendCreateUserData(
    email: String,
    fullName: String,
    username: String,
    password: String,
    pictureUri: File?,
    context: Context
){
    GlobalScope.launch(Dispatchers.IO) {
        try {
            withContext(Dispatchers.Main) {
                val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), pictureUri!!)
                val part = MultipartBody.Part.createFormData("profile_picture", pictureUri!!.name, requestBody)
                val usernameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), username)
                val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
                val fullNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), fullName)
                val passwordBody = RequestBody.create("text/plain".toMediaTypeOrNull(), password)
                val response = userService.createUser(usernameBody, emailBody, fullNameBody, passwordBody, part)
                if (response != null) {

                    Log.d("Created:","${response.email}")
                    Toast.makeText(context, R.string.Sing, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, R.string.error_signup, Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context,R.string.error_signup, Toast.LENGTH_SHORT).show()
                Log.d("Create error:", "${e.message}")
            }
        }
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