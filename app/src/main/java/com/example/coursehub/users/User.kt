package com.example.coursehub.users

import com.google.gson.annotations.SerializedName

/**
 * Data class to add an additional fields to FireStore collection
 */
data class User(
    val username:String,
    val email: String,
    @SerializedName("full_name")
    val fullName:String,
    val password:String
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