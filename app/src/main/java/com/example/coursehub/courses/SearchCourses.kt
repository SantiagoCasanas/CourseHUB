package com.example.coursehub.courses

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.coursehub.users.CourseData
import com.example.coursehub.users.TopicData
import kotlinx.coroutines.runBlocking

@Composable
fun SearchCourses(navController: NavController,
            modifier: Modifier = Modifier,
            context: Context = LocalContext.current
){
    val auth = Auth()
    auth.tokenManager = TokenManager(context)
    val courses = remember { runBlocking { auth.retrieveCourses() } }
    val topics = remember { runBlocking { auth.retrieveTopics() } }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.Backgorund_down))
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(id = R.string.search_title),
                fontSize = 40.sp,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold
            )
            SearchBar(onSearch = {})

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = stringResource(id = R.string.courses_suggestions),
                fontSize = 25.sp,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = colorResource(id = R.color.Backgorund_down),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                scrollableListSuggestCourses(courses, topics, navController)
            }
        }
    }
    Bar(navController = navController)
}

@Composable
fun courseSuggestRow(courseId:String, title:String, author:String, topic:String, topics:List<TopicData>?, navController: NavController){
    val topicObject = topics!!.find { it.id == topic }
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .background(
                color = colorResource(id = R.color.Background_up),
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .clickable {
                Log.d("ClickEvent", "Clicked on Row")
                val routeWithParam = "${Screens.TakeCourse.name}/$courseId"
                navController.navigate(routeWithParam)
            }
    ) {
        Column {
            Icon(imageVector = Icons.Default.Book,
                contentDescription = null,
                tint = colorResource(id = R.color.white),
                modifier = Modifier
                    .size(80.dp)
            )
        }
        Spacer(modifier = Modifier.width(25.dp))
        Column {
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = title,color = colorResource(id = R.color.white))
            Row {
                Icon(imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(20.dp)
                )
                Text(text = author, color = colorResource(id = R.color.white))
            }
            Text(text = topicObject!!.topic,color = colorResource(id = R.color.white))
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun scrollableListSuggestCourses(itemsList: List<CourseData>?, topics:List<TopicData>?, navController: NavController) {
    LazyColumn {
        items(itemsList!!) { item ->
            courseSuggestRow(item.id,item.tittle,item.authorUsername,item.topic, topics!!,navController)
        }
    }
}