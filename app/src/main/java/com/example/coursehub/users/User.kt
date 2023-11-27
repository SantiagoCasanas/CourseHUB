package com.example.coursehub.users

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