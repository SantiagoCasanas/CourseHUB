package com.example.coursehub.users


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class UserViewModel:ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun createUserWithEmailAndPassword(email: String, password: String){
        if (_loading.value==false){
            _loading.value=true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    task ->
                    if(task.isSuccessful){
                        Log.d("User","user created")
                    }else{
                        Log.d("User", "error: ${task.result.toString()}")
                    }
                    _loading.value =false
                }
        }
    }
     fun signUp( email: String, fullName: String, username: String, password:String ){
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
                createUserWithEmailAndPassword(email, password)
                Log.d("user created", "userId: ${it.id}")
            }
            .addOnFailureListener{
                Log.d("user error", "error: ${it}")
            }
    }

    fun logIn(email:String, password: String) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Log.d("User","te has logueado")
                    }else{
                        Log.d("User", "ocurrio un error: ${task.result.toString()}")
                    }
                }
        }catch (e: Exception){
            Log.d("User", "ocurrio un error: ${e.message}")
        }
    }
}

