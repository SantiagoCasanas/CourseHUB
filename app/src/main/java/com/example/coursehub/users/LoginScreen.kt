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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun LogInScreen(
    navController: NavController,
    context: Context = LocalContext.current
) {
    val login = Auth()
    login.tokenManager = TokenManager(context)
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
                text = stringResource(id = R.string.Log_in),
                fontSize = 40.sp,
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
                Spacer(modifier = Modifier.height(15.dp))
                Image(
                    painter= painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    )
                UserLoginForm() { username, password ->
                    runBlocking {
                        launch(Dispatchers.IO) {
                            login.sendLoginUserData(context,username,password){
                                Toast.makeText(context, R.string.Log, Toast.LENGTH_SHORT).show()
                                navController.navigate(Screens.HomeScreen.name)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.No_account),
                            color = Color.White,
                        )
                        Text(
                            text = stringResource(id = R.string.Sign_up),
                            modifier = Modifier
                                .clickable { navController.navigate(Screens.SignUpScreen.name) }
                                .padding(start = 5.dp),
                            color = colorResource(id = R.color.Buttom)
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.forgot_password),
                        modifier = Modifier
                            .clickable { navController.navigate(Screens.RecoverPassword.name) }
                            .padding(start = 5.dp),
                        color = colorResource(id = R.color.Buttom))
                }
            }
        }
    }
}

@Composable
fun UserLoginForm( onDone: (String, String) -> Unit = { _, _ -> }) {
    val username = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    val textPass = stringResource(id = R.string.password_advice)
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        // Entrada de User
        UsernameInput(label= stringResource(id = R.string.username), fieldState = username)

        // Entrada de Password

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
        // Botón de envío
        SubmitButton(textId = stringResource(id = R.string.Log_in),isEnabled = true) {
            onDone(username.value.trim(), password.value.trim())

        }
    }
}