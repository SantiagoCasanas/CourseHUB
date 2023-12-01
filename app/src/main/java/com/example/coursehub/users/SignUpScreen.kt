package com.example.coursehub.users


import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import coil.compose.rememberImagePainter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.coursehub.navigation.Screens
import com.example.coursehub.network.sendCreateUserData
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File


@Composable
fun SignUpScreen(
    navController: NavController,
    context: Context = LocalContext.current
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val arguments = navBackStackEntry?.arguments
    val imagePath = arguments?.getString("imagePath")
    Log.d("camera photo:","$imagePath")
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
                text = stringResource(id = R.string.Sign_up),
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
                ScrollableUserCreateForm(imagePath,navController, context) { email, fullName, username,password, picture ->
                    runBlocking {
                        launch(Dispatchers.IO) {
                            sendCreateUserData(email,fullName,username,password, picture, context)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScrollableUserCreateForm(path: String?,navController: NavController, context: Context, onDone: (String, String, String, String, File?) -> Unit = { _, _, _, _, _ -> }) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            // Área desplazable
            UserCreateForm(path,navController, context, onDone)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.go_login),
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Screens.LoginScreen.name)
                        }
                        .padding(16.dp),
                    color = colorResource(id = R.color.Buttom),
                    fontWeight = FontWeight.Bold
                )
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
                            text = stringResource(id = R.string.Have_account),
                            color = Color.White,
                        )
                        Text(
                            text = stringResource(id = R.string.Log_in),
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(Screens.LoginScreen.name)
                                }
                                .padding(start = 5.dp),
                            color = colorResource(id = R.color.Buttom)
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.forgot_password),
                        modifier = Modifier
                            .clickable { navController.navigate(Screens.RecoverPassword.name) }
                            .padding(start = 5.dp),
                        color = colorResource(id = R.color.Buttom)
                    )
                }
            }
        }
    }
}

@Composable
fun UserCreateForm(path:String?,navController: NavController,context: Context, onDone: (String, String, String, String, File?) -> Unit = { email, fullname, username, pwd, picture -> }) {
    val isCheckedList = remember { List(3) { mutableStateOf(false) } }
    val emailState = rememberSaveable { mutableStateOf("") }
    val passwordState = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    val fullNameState = rememberSaveable { mutableStateOf("") }
    val userNameState = rememberSaveable { mutableStateOf("") }
    val textPass = stringResource(id = R.string.password_advice)
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    Log.d("camera photo:","$path")

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val file = File(context.filesDir, "temp_image.png")
            Log.d("File","${file}")
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    file.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                selectedImageUri.value = uri
            } catch (e: Exception) {
                Log.d("CreateForm Image error:", "error: ${e.message}")
            }
        }
    }

    Column( horizontalAlignment = Alignment.CenterHorizontally) {
        Log.d("UserCreateForm", "Valor de selectedImageUri: ${selectedImageUri.value}")
        selectedImageUri.value?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .background(Color.LightGray)
            )
        }
        Row {
            Button(
                onClick = {
                    navController.navigate(Screens.CameraView.name)
                },
                colors = ButtonDefaults.buttonColors( containerColor = colorResource(id = R.color.Buttom))
            ) {
                Text(stringResource(id = R.string.take_photo), color = colorResource(id = R.color.white))
            }
            Button(
                onClick = {
                    launcher.launch("image/*")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.Buttom),
                    contentColor = colorResource(id = R.color.white)
                )
            ) {
                Text(stringResource(id = R.string.image_upload), color = colorResource(id = R.color.white))
            }
        }
        EmailInput(label = stringResource(id = R.string.email), emailState = emailState)
        NameInput(label = stringResource(id = R.string.fullname), fieldState = fullNameState)
        UsernameInput(label = stringResource(id = R.string.username), fieldState = userNameState)

        if (passwordState.value.length < 8) {
            Text(
                text = textPass,
                color = Color.Red
            )
        }

        PasswordInput(
            passwordSate = passwordState,
            labelId = stringResource(id = R.string.password),
            passwordVisible = passwordVisible
        )
        CheckButtonForRegister(isCheckedList = isCheckedList, index = 0,text = stringResource(id = R.string.terms), dialogtitle = stringResource(
            id = R.string.terms
        ), dialogText = stringResource(id = R.string.terminos_y_condiciones))
        CheckButtonForRegister(isCheckedList = isCheckedList, index = 1, text = stringResource(id = R.string.politics), dialogtitle = stringResource(
            id = R.string.politics
        ), dialogText = stringResource(id = R.string.politics_text))
        CheckButtonForRegister(isCheckedList = isCheckedList, index = 2, text = stringResource(id = R.string.data), dialogtitle = stringResource(
            id = R.string.data
        ), dialogText = stringResource(id = R.string.data_text))
        SubmitButton(
            textId = stringResource(id = R.string.Sign_up),
            isEnabled = isCheckedList.all { it.value }
        ) {
            if (isCheckedList.all { it.value }) {
                val file = File(context.filesDir, "temp_image.png")
                Log.d("File", "${file}")
                onDone(
                    emailState.value.trim(),
                    fullNameState.value.trim(),
                    userNameState.value.trim(),
                    passwordState.value.trim(),
                    file
                )
            } else {
                Toast.makeText(context, "select all checkboxes", Toast.LENGTH_SHORT).show()
            }
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
fun SubmitButton(textId: String,  isEnabled: Boolean,onClick: ()->Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(1f)
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        enabled=isEnabled,
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
            }
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

@Composable
fun CheckButtonForRegister(isCheckedList: List<MutableState<Boolean>>, index: Int,text: String, dialogtitle: String, dialogText: String
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                showDialog = true
            }
    ) {
        Checkbox(
            checked = isCheckedList[index].value,
            onCheckedChange = { isCheckedList[index].value = it },
            modifier = Modifier.align(CenterVertically),
            colors = androidx.compose.material3.CheckboxDefaults.colors(colorResource(id = R.color.Buttom))
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = text,
            modifier = Modifier
                .align(CenterVertically)
                .padding(5.dp)
                .clickable {
                    showDialog = true
                }
                .weight(1f)
                .wrapContentWidth(Alignment.Start),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = dialogtitle)
            },
            text = {
                Text(text = dialogText)
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text(stringResource(id = R.string.accept))
                }
            }
        )
    }
}



