package com.example.coursehub.users

import android.icu.text.CaseMap.Title
import android.net.Uri
import com.google.gson.annotations.SerializedName

data class User(
    val username:String,
    val email: String,
    @SerializedName("full_name")
    val fullName:String,
    val password:String,
    @SerializedName("profile_picture")
    val profilePicture: Uri?
)

data class UserCreateResponse(
    val username: String?,
    val email: String?,
    @SerializedName("full_name")
    val fullName:String?,
    @SerializedName("profile_picture")
    val profilePicture: String?
)

data class LoginUser(
    val username: String,
    val password: String
)

data class LoginResponse(
    val id: String,
    val refresh: String,
    val access:String
)

data class UserInfo(
    val username: String?,
    val email: String?,
    @SerializedName("full_name")
    val fullName:String?,
    @SerializedName("profile_picture")
    val profilePicture: String?
)

data class UserInfoResponse(
    val email: String?,
    @SerializedName("full_name")
    val fullName:String?,
    val username: String?
)

data class TokenRespose(
    val detail: String?,
    val error: String?,
    val profile_picture: String?
)

data class GetTokenData(
    val email: String
)
data class ResetPassData(
    val email: String,
    val token: String,
    val password:String
)

data class LogoutData(
    @SerializedName("refresh_token")
    val refresh: String
)

data class LogoutResponse(
    val detail: String?,
    val code: String?,
    val messages: String?
)

data class CreateCourseData(
    val tittle: String,
    val description: String,
    val topic: String
)

data class CourseData(
    val author : String,
    val topic : String,
    val tittle: String,
    val description: String,
    val calification: String,
    @SerializedName("number_of_chapters")
    val chapters: String
)

data class TopicData(
    val id: String,
    @SerializedName("topic_name")
    val topic : String,
)