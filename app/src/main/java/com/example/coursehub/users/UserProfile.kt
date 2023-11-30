package com.example.coursehub.users

import android.content.Context
import androidx.compose.material3.Button
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
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults

@Composable
fun ProfileViewUser(navController: NavController, modifier: Modifier = Modifier, context: Context = LocalContext.current){
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
            Spacer(modifier = Modifier.height(50.dp))

            ButtonWithArrow(
                text = stringResource(id = R.string.edit),
                onClick = { navController.navigate(Screens.UpdateScreen.name) }
            )

            ButtonWithArrow(
                text = stringResource(id = R.string.settings),
                onClick = { navController.navigate(Screens.SettingsScreen.name) }
            )

            ButtonWithArrow(
                text = stringResource(id = R.string.help),
                onClick = { /* TODO: Acción para Help */ }
            )

            // Espacio adicional al final
            Spacer(modifier = Modifier.height(200.dp))

            // Botón de cierre de sesión
            LogoutButton(
                textId = stringResource(id = R.string.logout),
                onClick = { /* TODO: Acción para cerrar sesión */ }
            )
        }
    }

    // Row con iconos de navegación
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // Iconos de navegación
        IconButton(onClick = { navController.navigate(Screens.HomeScreen.name) },
            modifier = Modifier
                .padding(8.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(id = R.string.home_title),
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = stringResource(id = R.string.home_title),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.white)
                )
            }
        }

        IconButton(onClick = { /* TODO: Acción para Course */ },
            modifier = Modifier
                .padding(8.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = stringResource(id = R.string.course_title),
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = stringResource(id = R.string.course_title),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.white)
                )
            }
        }

        IconButton(onClick = { /* TODO: Acción para Search */ },
            modifier = Modifier
                .padding(8.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_title),
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = stringResource(id = R.string.search_title),
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
                    contentDescription = stringResource(id = R.string.account_title),
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = stringResource(id = R.string.account_title),
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
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors( containerColor = colorResource(id = R.color.Backgorund_down))

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    fontSize = 25.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}

@Composable
fun LogoutButton(textId: String, onClick: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = stringResource(id = R.string.confirm))},
            text = { Text(text = stringResource(id = R.string.confirm_question)) },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onClick()
                    }
                ) {
                    Text(text = stringResource(id = R.string.si))
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
    Button(
        onClick = { showDialog = true },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(1f)
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors( containerColor = colorResource(id = R.color.Background_up))
    ) {
        Text(
            text = textId,
            fontSize = 25.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}