package com.example.coursehub.users

import android.Manifest
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.coursehub.navigation.Screens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.util.concurrent.Executor


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun openCamera(navController: NavController){
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val context = LocalContext.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }
    val lifeCycle = LocalLifecycleOwner.current
    LaunchedEffect(Unit){
        permissionState.launchPermissionRequest()
    }
    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButton(onClick = {
            val executor  = ContextCompat.getMainExecutor(context)
            takePicture(cameraController, executor){filePath->
                val routeWithImage = "${Screens.SignUpScreen.name}?imagePath=$filePath"
                Log.d("camera photo:","$routeWithImage")
                navController.navigate(routeWithImage)
                //navController.navigate(Screens.SignUpScreen.name)
            }
        }) {
        }
    }) {
        if (permissionState.status.isGranted){
            camera(cameraController,lifeCycle, modifier = Modifier.padding(it))
        }else{
            Text(text = "cy", modifier = Modifier.padding(it))
        }
    }
}
@Composable
fun camera(cameraController: LifecycleCameraController, lifeCycle: LifecycleOwner, modifier: Modifier = Modifier){
    cameraController.bindToLifecycle(lifeCycle)
    AndroidView(factory = {context->
        val previewView = PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
        previewView.controller= cameraController
        previewView
    })
}

fun takePicture(cameraController: LifecycleCameraController, executor: Executor, onImageCaptured:(String)->Unit){
    val file = File.createTempFile("profile_picture", ".png")
    val outPutDirectory = ImageCapture.OutputFileOptions.Builder(file).build()
    cameraController.takePicture(outPutDirectory, executor, object : ImageCapture.OnImageSavedCallback{
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            Log.d("Path:", "$file")
            val filePath = file.absolutePath // Ruta del archivo de imagen capturada
            onImageCaptured(filePath)
        }
        override fun onError(exception: ImageCaptureException) {
            TODO("Not yet implemented")
        }
    })
}