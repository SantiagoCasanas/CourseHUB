package com.example.coursehub.courses

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coursehub.R
import com.example.coursehub.navigation.Screens
import com.example.coursehub.navigationbar.Bar
import com.example.coursehub.network.Auth
import com.example.coursehub.network.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun createChapterScreen(
    navController: NavController,
    course:String?,
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,
){
    val auth = Auth()
    auth.tokenManager = TokenManager(context)
    Log.d("Course","$course")
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.Backgorund_down))
    ) {
        Column (modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start){
            Text(
                text = stringResource(id = R.string.course_title),
                fontSize = 40.sp,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold
            )
            SearchBar(onSearch = {})
        }
        CreateChapterForm(course,modifier){ course,title, description ->
            runBlocking {
                launch(Dispatchers.IO) {
                    auth.createNewChapter(course, title,description){
                        Toast.makeText(context,"Chapter created", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screens.MyCourses.name)
                    }
                }
            }
        }
    }
    Bar(navController = navController)
}
@Composable
fun CreateChapterForm(courseId:String?, modifier: Modifier = Modifier, onDone: (String, String, String) -> Unit = { _, _, _ -> }){

    val chapterTitle = rememberSaveable { mutableStateOf("") }

    val content = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.new_chapter),
            fontSize = 20.sp,
            color = colorResource(id = R.color.white),
            fontWeight = FontWeight.Bold
        )

        DescriptionField(label = stringResource(id = R.string.tittle), fieldState = chapterTitle)

        DescriptionField(label = stringResource(id = R.string.content), fieldState = content)


        Button(
            onClick = {
                onDone(courseId!!, chapterTitle.value.trim(), content.value.trim())
            },
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.add_chapter))
        }
    }
}