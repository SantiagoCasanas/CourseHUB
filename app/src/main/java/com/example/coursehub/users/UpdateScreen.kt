package com.example.coursehub.users

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.test.core.app.ActivityScenario.launch
import coil.compose.rememberImagePainter
import java.io.File
import coil.compose.rememberImagePainter
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun ProfileView(navController: NavController, modifier: Modifier = Modifier, context: Context = LocalContext.current){
    val info = Auth()
    info.tokenManager = TokenManager(context)
    val userInfo = remember { runBlocking { info.getUserInfo() } }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.Backgorund_down))
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp)){
            Text(
                text = stringResource(id = R.string.account),
                fontSize = 40.sp,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp),
            contentAlignment = Alignment.Center
        ) {
            userupdateForm2(userInfo = userInfo, context){ email, fullName, username, picture ->
                runBlocking {
                    launch(Dispatchers.IO) {
                        info.update_info(email, fullName ,username, picture)
                    }
                }
            }
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
fun userUpdateForm(
    userInfo: UserInfo?,
    onDone: (String,String, String) -> Unit ={email,fullname, username->}
) {
    val url = userInfo!!.profilePicture

    val email = rememberSaveable {
        mutableStateOf("${userInfo!!.email}")
    }

    val fullname = rememberSaveable { mutableStateOf("${userInfo!!.fullName}") }
    val username = rememberSaveable { mutableStateOf("${userInfo!!.username}") }

    Log.d("Data form:", "${fullname},${username},${email}")

    Column (horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Image(
            painter = rememberImagePainter(url),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .background(Color.LightGray)
        )
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
        SubmitButton(
            textId = "Edit Account",isEnabled = true
        ){
            onDone(email.value.trim(),fullname.value.trim(), username.value.trim())
        }

    }
}

@Composable
fun userupdateForm2(userInfo: UserInfo?,context: Context, onDone: (String, String, String, File?) -> Unit = { _, _, _, _ -> }) {

    val url = userInfo!!.profilePicture
    val emailState = rememberSaveable {  mutableStateOf("${userInfo!!.email}") }
    val fullNameState = rememberSaveable { mutableStateOf("${userInfo!!.fullName}") }
    val userNameState = rememberSaveable { mutableStateOf("${userInfo!!.username}") }

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val defaultImageUri = runBlocking {
        val imageDeferred = async(Dispatchers.IO) {
            downloadImageAndSaveAsTempFile(context, url!!)
        }
        imageDeferred.await()
    }
    Log.d("URL:", "${defaultImageUri}")

    Log.d("Data:", "${emailState},${fullNameState},${userNameState}")
    val updatedUri by rememberUpdatedState(selectedImageUri.value)

    val finalUri = updatedUri ?: defaultImageUri

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
                onDone(
                    emailState.value.trim(),
                    fullNameState.value.trim(),
                    userNameState.value.trim(),
                    file
                )
            } catch (e: Exception) {
                Log.d("UserCreateForm", "error: ${e.message}")
            }
        }
    }


    Column (horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Image(
            painter = rememberImagePainter(finalUri),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .background(Color.LightGray)
        )
        Button(
            onClick = {
                launcher.launch("image/*")
            }
        ) {
            Text(stringResource(id = R.string.image_edit))
        }
        EmailInput(label=stringResource(id = R.string.email),
            emailState = emailState
        )
        NameInput(
            label = stringResource(id = R.string.fullname),
            fieldState = fullNameState
        )
        UsernameInput(
            label = stringResource(id = R.string.username),
            fieldState = userNameState
        )
        SubmitButton(
            textId = stringResource(id = R.string.Edit_account),isEnabled = true
        ) {
            val file = File(context.filesDir, "temp_image.png")
            Log.d("File","${file}")
            onDone(
                emailState.value.trim(),
                fullNameState.value.trim(),
                userNameState.value.trim(),
                file
            )
        }
    }
}

suspend fun downloadImageAndSaveAsTempFile(context: Context, imageUrl: String): File? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()

            val inputStream: InputStream = connection.inputStream
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

            val tempImageFile = File(context.filesDir, "temp_image.png")
            val fileOutputStream = FileOutputStream(tempImageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            tempImageFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}