package com.example.coursehub.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coursehub.users.GreetingImage
import com.example.coursehub.users.SignUpScreen
import com.example.coursehub.users.profileView
import com.example.coursehub.users.ProfileViewUser

@Composable
fun CourseHubNavigate(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.LoginAndSignUpScreen.name){
        composable(Screens.LoginAndSignUpScreen.name){
            SignUpScreen(navController = navController)
        }
        composable(Screens.HomeScreen.name){
            GreetingImage(navController = navController)
        }
        composable(Screens.UserProfile.name){
            ProfileViewUser(navController = navController)
        }
        composable(Screens.UpdateScreen.name){
            profileView(navController = navController)
        }
    }
}
