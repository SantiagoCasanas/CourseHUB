package com.example.coursehub.users


import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserViewModel:ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private fun signUp( email: String, fullName: String, username: String, password:String ){
        val userId = auth.currentUser?.uid
        val user = Users(
            userId.toString(),
            email,
            fullName,
            username,
            password
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("user created", "userId: ${it.id}")
            }
            .addOnFailureListener{
                Log.d("user error", "error: ${it}")
            }
    }
}

