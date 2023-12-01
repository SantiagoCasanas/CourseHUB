package com.example.coursehub.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coursehub.users.GreetingImage
import com.example.coursehub.users.LogInScreen
import com.example.coursehub.users.SignUpScreen
import com.example.coursehub.users.ProfileView
import com.example.coursehub.users.ProfileViewUser
import com.example.coursehub.users.SettingsView
import com.example.coursehub.users.openCamera
import com.example.coursehub.users.recoverPasswordScreen
import com.example.coursehub.users.resetPasswordScreen

@Composable
fun CourseHubNavigate(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.LoginScreen.name){
        composable(Screens.SignUpScreen.name){
            SignUpScreen(navController = navController)
        }
        composable(Screens.HomeScreen.name){
            GreetingImage(navController = navController)
        }
        composable(Screens.UserProfile.name){
            ProfileViewUser(navController = navController)
        }
        composable(Screens.UpdateScreen.name){
            ProfileView(navController = navController)
        }
        composable(Screens.SettingsScreen.name){
            SettingsView(navController = navController)
        }
        composable(Screens.RecoverPassword.name){
            recoverPasswordScreen(navController = navController)
        }
        composable(Screens.ResetPassword.name){
            resetPasswordScreen(navController = navController)
        }
        composable(Screens.CameraView.name){
            openCamera(navController = navController)
        }
        composable(Screens.LoginScreen.name){
            LogInScreen(navController = navController)
        }
    }
}
