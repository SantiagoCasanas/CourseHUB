package com.example.coursehub.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coursehub.courses.Courses
import com.example.coursehub.courses.CreateCourseForm
import com.example.coursehub.courses.SearchCourses
import com.example.coursehub.courses.TakeCourseContent
import com.example.coursehub.courses.chapterContent
import com.example.coursehub.courses.chaptersView
import com.example.coursehub.courses.courseContent
import com.example.coursehub.courses.createChapterScreen
import com.example.coursehub.courses.createCourseScreen
import com.example.coursehub.courses.myCourses
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
        composable(Screens.CreateCourses.name){
            createCourseScreen(navController = navController)
        }
        composable(Screens.CoursesView.name){
            Courses(navController = navController)
        }
        composable(Screens.MyCourses.name){
            myCourses(navController = navController)
        }
        composable(
            route = "${Screens.CreateChapter.name}/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) {
            val courseId = it.arguments?.getString("courseId")
            createChapterScreen(navController = navController, courseId)
        }
        composable(
            route = "${Screens.CourseContent.name}/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) {
            val courseId = it.arguments?.getString("courseId")
            courseContent(navController = navController, courseId)
        }
        composable(Screens.SearchCourses.name){
            SearchCourses(navController = navController)
        }
        composable(
            route = "${Screens.TakeCourse.name}/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ){
            val courseId = it.arguments?.getString("courseId")
            TakeCourseContent(navController = navController,courseId)
        }
        composable(
            route = "${Screens.ChaptersView.name}/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ){
            val courseId = it.arguments?.getString("courseId")
            chaptersView(navController = navController, courseId)
        }
        composable(
            route = "${Screens.ChapterContent.name}/{courseId}/{chapter}",
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("chapter") { type = NavType.StringType }
            )
        ){
            val courseId = it.arguments?.getString("courseId")
            val chapter = it.arguments?.getString("chapter")
            chapterContent(navController = navController, courseId, chapter)
        }
    }
}
