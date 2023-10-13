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
    // variable to use Firebase authentication and user creation services
    private val auth: FirebaseAuth = Firebase.auth
    //To verify that many users are not created at the same time
    private val _loading = MutableLiveData(false)

    fun createUserWithEmailAndPassword(email: String, password: String,fullName: String, username: String){
        // To verify any user are not being create
        if (_loading.value==false){
            // Firebase require a password of at least 6 characters
            if (password.length>=6){
                //the value is changed because a new user is being created
                _loading.value=true
                //call Firebase crateUser function with user password and email
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                            task ->
                        if(task.isSuccessful){
                            //if everything is correct,use another function to add the additional values
                            signUp(email, fullName, username)
                            Log.d("User","user created")
                        }else{
                            //message error
                            Log.d("User", "error: ${task.result.toString()}")
                        }
                        //The value is change because the user has already been created
                        _loading.value =false
                    }
            }else{
                //if the password is invalid
                Log.d("User", "The password must be at least 6 characters")
            }
        }
    }
    fun signUp( email: String, fullName: String, username: String ){
        //variable to get the userid assigned in Firebase createuser function
        val userId = auth.currentUser?.uid
        //create a map with user data
        val user = Users(
            userId.toString(),
            email,
            fullName,
            username
        ).toMap()
        //get the collection where the user data will be saved
        FirebaseFirestore.getInstance().collection("users")
            //add map with user data
            .add(user)
            .addOnSuccessListener {
                Log.d("user created", "userId: ${it.id}")
            }
            .addOnFailureListener{
                Log.d("user error", "error: ${it}")
            }
    }

    fun logIn(email:String, password: String) = viewModelScope.launch {
        try {
            //call the Firestore SignIn function with email and password
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Log.d("User","you're logged in")
                    }else{
                        Log.d("User", "an error has occurred: ${task.result.toString()}")
                    }
                }
        }catch (e: Exception){
            Log.d("User", "an error has occurred: ${e.message}")
        }
    }
}

