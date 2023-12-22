package com.example.coursehub.courses

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coursehub.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.coursehub.navigationbar.Bar
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import com.example.coursehub.navigation.Screens
import com.example.coursehub.network.Auth
import com.example.coursehub.network.TokenManager
import com.example.coursehub.users.TopicData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val topicsname = mutableListOf<String>()
var selectedIndex by mutableStateOf(0)
@Composable
fun createCourseScreen(
    navController: NavController,
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier
){
    val auth = Auth()
    auth.tokenManager = TokenManager(context)
    val topics = remember { runBlocking { auth.retrieveTopics() } }

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
        CreateCourseForm(modifier,topics){ title, description, topic ->
            runBlocking {
                launch(Dispatchers.IO) {
                    auth.createNewCourse(title,description, topic){
                        Toast.makeText(context,"Course created", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screens.CoursesView.name)
                    }
                }
            }
        }
    }
    Bar(navController = navController)

}
@Composable
fun CreateCourseForm( modifier: Modifier = Modifier,topics: List<TopicData>?, onDone: (String, String,String) -> Unit = { _, _, _ -> }){
    val title = rememberSaveable { mutableStateOf("") }

    val description = rememberSaveable { mutableStateOf("") }

    val topic = rememberSaveable { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.new_course),
            fontSize = 20.sp,
            color = colorResource(id = R.color.white),
            fontWeight = FontWeight.Bold
        )
        CourseName(label = stringResource(id = R.string.course_name), fieldState = title)

        DescriptionField(label = stringResource(id = R.string.course_description), fieldState = description)

        if (topics != null) {
            for (topic in topics){
                topicsname.add(topic.topic)
            }
        }
        dropdownMenu(items = topicsname.distinct())

        val chosenTopic = topicsname[selectedIndex]
        Log.d("chosen topic", chosenTopic)

        val topicObject = topics!!.find { it.topic == chosenTopic }
        Log.d("topic_id:","$topicObject")

        topic.value = topicObject!!.id


        Button(
            onClick = {
                onDone(title.value.trim(), description.value.trim(), topic.value.trim())
            },
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.add_course))
        }
    }
}
@Composable
fun CourseName(label: String, fieldState: MutableState<String>) {
    OutlinedTextField(
        value = fieldState.value,
        onValueChange = { fieldState.value = it },
        label = { Text(label,color=Color.White) },
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun DescriptionField(label: String, fieldState: MutableState<String>) {
    // Área de texto para la descripción
    OutlinedTextField(
        value = fieldState.value,
        onValueChange = { fieldState.value = it },
        label = { Text(label, color = Color.White) },
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }

    TextField(
        value = searchText,
        onValueChange = {
            searchText = it
            onSearch(it) // Llama a la función de búsqueda con el texto actualizado
        },
        placeholder = { Text("Find course", color = colorResource(id = R.color.white)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, // Reemplaza R.drawable.ic_search con tu propio ícono de lupa
                contentDescription = "Search icon",
                tint = colorResource(id =R.color.white)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    )
}

@Composable
fun dropdownMenu(items: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    //var selectedIndex by remember { mutableStateOf(0) }

    Box {
        Text(
            text = "Choose topic: ${items[selectedIndex]}",
            modifier = Modifier
                .clickable(onClick = { expanded = true })
                .padding(16.dp)
                .border(1.dp, color = colorResource(id = R.color.black))
                .padding(8.dp),
            color = colorResource(id = R.color.white)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(16.dp)
        ) {
            items.forEachIndexed { index, text ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                }) {
                    Text(text = text)
                }
            }
        }
    }
}