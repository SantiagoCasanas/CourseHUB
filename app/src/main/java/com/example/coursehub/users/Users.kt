package com.example.coursehub.users

data class Users(val userid: String, val email:String, val fullName:String, val username:String, val password:String){
    fun toMap():MutableMap<String, Any>{
        return mutableMapOf(
            "email" to  this.email,
            "fullname" to this.fullName,
            "username" to this.username,
            "password" to this.password
        )
    }
}
