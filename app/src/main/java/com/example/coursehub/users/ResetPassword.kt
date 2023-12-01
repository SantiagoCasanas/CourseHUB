package com.example.coursehub.users

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.coursehub.network.resetPassword
import com.example.coursehub.network.sendRecoverCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File


@Composable
fun resetPasswordScreen(
    navController: NavController,
    context: Context = LocalContext.current
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.Backgorund_down)) // Color azul
                .padding(40.dp)
        ) {
            Text(
                text = "Recover password",
                fontSize = 35.sp,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold
            )
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
                Image(
                    painter= painterResource(id = R.drawable.logo),
                    contentDescription = null
                )
                resetPassForm(){email, token, password->
                    runBlocking {
                        launch(Dispatchers.IO) {
                            resetPassword(email,token, password){
                                navController.navigate(Screens.LoginAndSignUpScreen.name)
                            }
                        }
                    }
                }
                Text(
                    text = stringResource(id = R.string.Log_in),
                    modifier = Modifier
                        .clickable { navController.navigate(Screens.LoginAndSignUpScreen.name)}
                        .padding(start = 5.dp),
                    color = colorResource(id = R.color.Buttom)
                )
            }
        }
    }
}
@Composable
fun resetPassForm( onDone: (String, String, String) -> Unit = { _, _, _ -> }) {
    val email = rememberSaveable { mutableStateOf("") }
    val token = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    val textPass = stringResource(id = R.string.password_advice)

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        UsernameInput(label= stringResource(id = R.string.email), fieldState = email)
        UsernameInput(label= stringResource(id = R.string.token), fieldState = token)

        if (password.value.length < 8) {
            Text(
                text = textPass,
                color = Color.Red
            )
        }
        PasswordInput(
            passwordSate = password,
            labelId = stringResource(id = R.string.password),
            passwordVisible = passwordVisible
        )
        SubmitButton(textId = stringResource(id = R.string.reset),isEnabled = true) {
            onDone(
                email.value.trim(),
                token.value.trim(),
                password.value.trim()
            )
        }
    }
}