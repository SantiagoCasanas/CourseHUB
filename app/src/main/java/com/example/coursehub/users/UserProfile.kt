package com.example.coursehub.users

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.coursehub.network.Auth
import com.example.coursehub.network.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import coil.compose.rememberImagePainter
import java.io.File
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.ArrowForward
import coil.compose.rememberImagePainter

@Composable
fun profileViewUser(navController: NavController, modifier: Modifier = Modifier, context: Context = LocalContext.current){
    val info = Auth()
    info.tokenManager = TokenManager(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.Backgorund_down))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.account),
                fontSize = 40.sp,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold
            )

            // Espacio adicional entre el texto y los botones
            Spacer(modifier = Modifier.height(16.dp))

            ButtonWithArrow(
                text = "Edit Profile",
                onClick = { navController.navigate(Screens.UpdateScreen.name) }
            )

            ButtonWithArrow(
                text = "Settings",
                onClick = { /* TODO: Acción para Settings */ }
            )

            ButtonWithArrow(
                text = "Help",
                onClick = { /* TODO: Acción para Help */ }
            )
        }
    }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        IconButton(onClick = { navController.navigate(Screens.HomeScreen.name) },
            modifier = Modifier
                .padding(8.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "Home",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.white)
                )
            }
        }
        IconButton(onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(8.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = "Course",
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "Course",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.white)
                )
            }
        }
        IconButton(onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(8.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "Search",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.white)
                )
            }
        }
        IconButton(onClick = { navController.navigate(Screens.UserProfile.name) },
            modifier = Modifier
                .padding(8.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Account",
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "Account",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.white)
                )
            }
        }
    }
}

@Composable
fun ButtonWithArrow(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .weight(1f)
                .background(color = colorResource(id = R.color.Backgorund_down)) // Hacer el fondo transparente
        ) {
            Text(
                text = text,
                fontSize = 25.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Flecha a la derecha
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = colorResource(id = R.color.white),
            modifier = Modifier
                .size(24.dp)
        )
    }
}
