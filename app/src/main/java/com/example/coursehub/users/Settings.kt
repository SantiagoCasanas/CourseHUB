package com.example.coursehub.users

import android.content.Context
import android.net.Uri
import androidx.compose.material3.Button
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import coil.compose.rememberImagePainter
import com.example.coursehub.navigationbar.Bar

@Composable
fun SettingsView(navController: NavController, modifier: Modifier = Modifier, context: Context = LocalContext.current){
    val auth = Auth()
    auth.tokenManager = TokenManager(context)
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
                text = stringResource(id = R.string.settings),
                fontSize = 40.sp,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold
            )

            // Espacio adicional entre el texto y los botones
            Spacer(modifier = Modifier.height(50.dp))
            deleteButton {
                runBlocking {
                    launch(Dispatchers.IO) {
                        auth.deactivateUser(context){
                            navController.navigate(Screens.LoginScreen.name)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            /*ButtonWithArrow(
                text = stringResource(id = R.string.inactivate),
                onClick = { /* TODO: Acción para Settings */ }
            )*/
        }
    }

    Bar(navController = navController)
}

@Composable
fun deleteButton(onClick: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = stringResource(id = R.string.delete_account))},
            text = { Text(text = stringResource(id = R.string.deleteAccount)) },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onClick()
                    }
                ) {
                    Text(text = stringResource(id = R.string.yes))
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text(text = stringResource(id = R.string.no))
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }
    ButtonWithArrow(
        text = stringResource(id = R.string.delete),
        onClick = { showDialog = true },
    )
}
