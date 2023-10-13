package com.example.coursehub.users

/**
 * Data class to add an additional fields to FireStore collection
 */
data class Users(val userid: String, val email:String, val fullName:String, val username:String){
    /**
     * We need to transform the user data to key-value
     */
    fun toMap():MutableMap<String, Any>{
        return mutableMapOf(
            "userId" to this.userid,
            "email" to  this.email,
            "fullname" to this.fullName,
            "username" to this.username
        )
    }
}
