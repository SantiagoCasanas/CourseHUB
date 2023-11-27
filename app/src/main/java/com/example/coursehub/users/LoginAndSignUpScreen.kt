package com.example.coursehub.users

import android.content.Context
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursehub.R
import androidx.navigation.NavController
import com.example.coursehub.navigation.Screens
import com.example.coursehub.network.Login
import com.example.coursehub.network.TokenManager
import com.example.coursehub.network.sendCreateUserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking



@Composable
fun SignUpScreen(
    navController: NavController,
    context: Context = LocalContext.current
) {
    val login = Login()
    login.tokenManager = TokenManager(context)
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }
    val text = if (showLoginForm.value) stringResource(id = R.string.Log_in) else stringResource(id = R.string.Sign_up)
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
                text = text,
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                if (showLoginForm.value) {
                    UserLoginForm(isCreatedAccount = false) { username, password ->
                        runBlocking {
                            launch(Dispatchers.IO) {
                                login.sendLoginUserData(username,password){
                                    navController.navigate(Screens.HomeScreen.name)
                                }

                            }
                        }
                    }
                } else {
                    UserCreateForm(isCreatedAccount = true) { email, fullName, username,password ->
                        runBlocking {
                            launch(Dispatchers.IO) {
                                sendCreateUserData(email,fullName,username,password)
                            }
                        }
                        showLoginForm.value = true
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val text1 = if (showLoginForm.value) stringResource(id = R.string.No_account) else stringResource(id = R.string.Have_account)
                    val text2 = if (showLoginForm.value) stringResource(id = R.string.Sign_up) else stringResource(id = R.string.Log_in)

                    Text(
                        text = text1,
                        color = Color.White,
                    )
                    Text(
                        text = text2,
                        modifier = Modifier
                            .clickable { showLoginForm.value = !showLoginForm.value }
                            .padding(start = 5.dp),
                        color = colorResource(id = R.color.Buttom)
                    )
                }
            }
        }
    }
}
@Composable
fun UserLoginForm(isCreatedAccount: Boolean = false, onDone: (String, String) -> Unit = { _, _ -> }) {
    val username = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    val textPass= stringResource(id =R.string.password_advice )

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        // Entrada de Email
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
        SubmitButton(textId = if (isCreatedAccount) stringResource(id = R.string.Sign_up) else stringResource(id = R.string.Log_in)) {
            onDone(username.value.trim(), password.value.trim())
        }
    }
}


@Composable
fun UserCreateForm(isCreatedAccount: Boolean = false, onDone: (String,String, String, String) -> Unit ={email,fullname, username, pwd->}) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val fullname = rememberSaveable { mutableStateOf("") }
    val username = rememberSaveable { mutableStateOf("") }
    val textPass= stringResource(id =R.string.password_advice )

    Column (horizontalAlignment = Alignment.CenterHorizontally,
        ){
        EmailInput(label=stringResource(id = R.string.email),
            emailState = email
        )
        NameInput(
            label = stringResource(id = R.string.fullname),
            fieldState = fullname
        )
        UsernameInput(
            label = stringResource(id = R.string.username),
            fieldState = username
        )
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
        SubmitButton(
            textId = if(isCreatedAccount)stringResource(id = R.string.Sign_up) else stringResource(id = R.string.Log_in)
        ){
            onDone(email.value.trim(),fullname.value.trim(), username.value.trim(), password.value.trim())
        }

    }
}

@Composable
fun UsernameInput(label: String, fieldState: MutableState<String>) {
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
fun NameInput(label: String, fieldState: MutableState<String>) {
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
fun SubmitButton(textId: String, onClick: ()->Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(1f)
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.Buttom)
        )
    ) {
        Text(
            text = textId,
            fontSize = 25.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PasswordInput(passwordSate: MutableState<String>,
                  labelId: String,
                  passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if(passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()
    androidx.compose.material.OutlinedTextField(value = passwordSate.value,
        onValueChange ={passwordSate.value = it},
        label = { Text(text = labelId,color=Color.White) },
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if(passwordSate.value.isNotBlank()){
                PasswordVisibleIcon(passwordVisible)
            }else null
        }
    )
}

@Composable
fun PasswordVisibleIcon(passwordVisible: MutableState<Boolean>) {
    val image = if (passwordVisible.value)
        Icons.Default.VisibilityOff
    else
        Icons.Default.Visibility
    IconButton(onClick = { passwordVisible.value = !passwordVisible.value}) {
        Icon(imageVector = image, contentDescription ="", tint = Color.White)
    }
}

@Composable
fun EmailInput(emailState: MutableState<String>, label:String) {
    androidx.compose.material.OutlinedTextField(
        value = emailState.value,
        onValueChange = { emailState.value = it },
        label = { Text(label,color=Color.White) },
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}
