package com.example.coursehub.courses

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun TakeCourseContent(navController: NavController,
                  course:String?,
                  modifier: Modifier = Modifier,
                  context: Context = LocalContext.current
){
    val auth = Auth()
    auth.tokenManager = TokenManager(context)
    val topics = remember { runBlocking { auth.retrieveTopics() } }
    val courseDetail = remember { runBlocking { auth.retrieveCourse(course!!) } }
    Log.d("Course:","$course,$courseDetail")
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.Backgorund_down)) // Color azul
                .padding(40.dp)
        ) {
            if (courseDetail!=null){
                Text(
                    text = courseDetail.tittle,
                    fontSize = 30.sp,
                    color = colorResource(id = R.color.white),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 130.dp)
                .background(color = colorResource(id = R.color.Background_up))
                .clip(shape = RoundedCornerShape(15.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                course(
                    author = courseDetail!!.authorUsername,
                    topic = courseDetail.topic,
                    topics = topics,
                    description =courseDetail.description,
                    calification = courseDetail.calification,
                    chapters = courseDetail.chapters
                )

                Button(
                    onClick = {
                        runBlocking {
                            launch(Dispatchers.IO) {
                                auth.takeNewCourse(course!!){
                                    val routeWithParam = "${Screens.ChaptersView.name}/${courseDetail.id}"
                                    navController.navigate(routeWithParam)
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.Backgorund_down))
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = stringResource(id = R.string.chapter),
                            tint = colorResource(id = R.color.white),
                            modifier = Modifier
                                .size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = stringResource(id = R.string.start_course),
                            color = colorResource(id = R.color.white),
                            fontSize = 20.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
    Bar(navController = navController)
}