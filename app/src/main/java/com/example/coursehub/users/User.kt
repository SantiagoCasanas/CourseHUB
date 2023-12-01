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