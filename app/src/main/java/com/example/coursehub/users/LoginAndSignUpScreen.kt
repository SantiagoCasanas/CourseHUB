package com.example.coursehub.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun signUpScreen(
    viewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    val showLoginForm = rememberSaveable{
        mutableStateOf(true)
    }
    Surface(modifier = Modifier
        .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showLoginForm.value){
                Text(text = "Log In")
                UserLoginForm(
                    isCreatedAccount=false
                ){
                        email, password ->
                    viewModel.logIn(email,password)
                }
            }
            else{
                Text(text = "Sign Up")
                UserCreateForm(
                    isCreatedAccount = true
                ){
                        email, fullname, username, password ->
                    viewModel.signUp(email,fullname,username,password)
                    showLoginForm.value = true
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row (horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                val text1 = if (showLoginForm.value) "I don't have an account" else "I already have an account"
                val text2 = if (showLoginForm.value) "SignUp" else "Login"

                Text(text = text1)
                Text(text = text2,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp),
                    color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}
@Composable
fun UserLoginForm(isCreatedAccount: Boolean = false, onDone: (String,String) -> Unit ={email, pwd->}) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    Column (horizontalAlignment = Alignment.CenterHorizontally){

        EmailInput(
            emailState = email
        )
        PasswordInput(
            passwordSate = password,
            labelId = "Password",
            passwordVisible = passwordVisible
        )
        SubmitButton(
            textId = if(isCreatedAccount)"SignUp" else "Login"
        ){
            onDone(email.value.trim(), password.value.trim())
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
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        EmailInput(
            emailState = email
        )
        NameInput(
            label = "Full name",
            fieldState = fullname
        )
        UsernameInput(
            label = "Username",
            fieldState = username
        )
        PasswordInput(
            passwordSate = password,
            labelId = "Password",
            passwordVisible = passwordVisible
        )
        SubmitButton(
            textId = if(isCreatedAccount)"SignUp" else "Login"
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
        label = { Text(label) },
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
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun SubmitButton(textId: String, onClick: ()->Unit) {
    Button(onClick = onClick, modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth(),
        shape = CircleShape
    ) {
        Text(text = textId,
            modifier = Modifier
                .padding(5.dp))

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
        label = { Text(text = labelId) },
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
                passwordVisibleIcon(passwordVisible)
            }else null
        }
    )
}

@Composable
fun passwordVisibleIcon(passwordVisible: MutableState<Boolean>) {
    val image = if (passwordVisible.value)
        Icons.Default.VisibilityOff
    else
        Icons.Default.Visibility
    IconButton(onClick = { passwordVisible.value = !passwordVisible.value}) {
        Icon(imageVector = image, contentDescription ="")
    }
}

@Composable
fun EmailInput(emailState: MutableState<String>, labelId: String = "Email") {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email
    )
}

@Composable
fun InputField(valueState: MutableState<String>, labelId: String, keyboardType: KeyboardType, isSingleLine: Boolean = true) {
    androidx.compose.material.OutlinedTextField(value = valueState.value,
        onValueChange ={valueState.value=it},
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}