package com.example.coursehub.users
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coursehub.R
import com.example.coursehub.navigation.Screens
import com.example.coursehub.network.Auth
import com.example.coursehub.network.TokenManager
import com.example.coursehub.network.sendCreateUserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun CreateCourseForm(navController: NavController, modifier: Modifier = Modifier){
    val nameState = rememberSaveable { mutableStateOf("") }

    val descriptionState = rememberSaveable { mutableStateOf("") }

    // Columna principal
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CourseName(label = "Ingresa el nombre del curso", fieldState = nameState)

       DescriptionField(label = "Ingresa la descripción del curso", fieldState = descriptionState)

        Button(
            onClick = {
            },
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text(text = "Agregar Curso")
        }
    }
}
@Composable
fun CourseName(label: String, fieldState: MutableState<String>) {
    androidx.compose.material.OutlinedTextField(
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
    androidx.compose.material.OutlinedTextField(
        value = fieldState.value,
        onValueChange = { fieldState.value = it },
        label = { Text(label, color = Color.White) },
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}