package com.example.erp.android.UI.Screens.BottomNavScreens.Camera

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.lms.android.ui.Component.Cust_Btn
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraView(
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    // 1
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    // 2
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    // 3
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

        IconButton(
            modifier = Modifier.padding(bottom = 20.dp),
            onClick = {
                Log.i("kilo", "ON CLICK")
                takePhoto(
                    filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                    imageCapture = imageCapture,
                    outputDirectory = outputDirectory,
                    executor = executor,
                    onImageCaptured = onImageCaptured,
                    onError = onError
                )
            },
            content = {
                Icon(
                    imageVector = Icons.Sharp.Lens,
                    contentDescription = "Take picture",
                    tint = Color.White,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(1.dp)
                        .border(1.dp, Color.White, CircleShape)
                )
            }
        )
    }
}

private fun takePhoto(
    filenameFormat: String,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {

    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            Log.e("kilo", "Take photo error:", exception)
            onError(exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(photoFile)
            onImageCaptured(savedUri)
        }
    })
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }


//working wala
//    ..................................................
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CameraPreviewApp(context: Context) {
//    var imageUri by remember { mutableStateOf<Uri?>(null) }
//    var imagePath by remember { mutableStateOf<String?>(null) }
//    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
//
//    // Function to create an empty file to store the captured image
//    val createImageFile: () -> File = {
//        val storageDir: File? = context.getExternalFilesDir("Pictures")
//        File.createTempFile(
//            "JPEG_${System.currentTimeMillis()}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        )
//    }
//
//    // Launch camera to take a picture and save to the given Uri
//    val takePictureLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicture()
//    ) { success: Boolean ->
//        if (success) {
//            imageUri?.let { uri ->
//                imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
//                imagePath = uri.path // Store the image path
//            }
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("Camera Preview with Path") })
//        },
//        content = { paddingValues ->
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(MaterialTheme.colorScheme.onBackground)
//                    .padding(paddingValues),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                imageBitmap?.let {
//                    item {
//                        Image(
//                            bitmap = it.asImageBitmap(),
//                            contentDescription = "Captured Image",
//                            modifier = Modifier
//                                .size(300.dp)
//                                .padding(16.dp),
//                            contentScale = ContentScale.Fit
//                        )
//                    }
//                }
//
//
//                imagePath?.let {
//                    item {
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Text(text = "Image path: $it",
//                            color = Color.Black)
//                        Spacer(modifier = Modifier.height(16.dp))
//                    }
//                }
//
//
//                item {
//                    TextButton(onClick = {
//                        val imageFile = createImageFile()
//                        imageUri = FileProvider.getUriForFile(
//                            context,
//                            "${context.packageName}.fileprovider",
//                            imageFile
//                        )
//                        imageUri?.let { uri ->
//                            takePictureLauncher.launch(uri) // Ensure non-null URI
//                        }
//                    }) {
//                        Text(text = "Take Picture", color = Color.Blue)
//                    }
//                }
//                item{ Text(text = "OR", color = Color.Black,
//                    style = MaterialTheme.typography.labelMedium)}
//                item {
//                    FileButton(
//                        onFileSelected = {},
//                        title = "Select Image",
//                    )
//                }
//            }
//        }
//    )
//}
//        ..........................................................................


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPreviewApp(context: Context) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imagePath by remember { mutableStateOf<String?>(null) }
    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    // Function to create an empty file to store the captured image
    val createImageFile: () -> File = {
        val storageDir: File? = context.getExternalFilesDir("Pictures")
        File.createTempFile(
            "JPEG_${System.currentTimeMillis()}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    // Launch camera to take a picture and save to the given Uri
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            imageUri?.let { uri ->
                imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                imagePath = uri.path // Store the image path
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Camera Preview with Path") })
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onBackground)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                imageBitmap?.let {
                    item {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .size(300.dp)
                                .padding(16.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                imagePath?.let {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Image path: $it", color = Color.Black)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                item {
                    TextButton(onClick = {
                        val imageFile = createImageFile()
                        imageUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            imageFile
                        )
                        imageUri?.let { uri ->
                            takePictureLauncher.launch(uri) // Ensure non-null URI
                        }
                    }) {
                        Text(text = "Take Picture", color = Color.Blue)
                    }
                }

                item {
                    Text(
                        text = "OR",
                        color = Color.Black,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                item {
                     FileButton(
                        onFileSelected = { uri ->
                            imageUri = uri
                            imageUri?.let {
                                imageBitmap =
                                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                                imageBitmap =
                                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                                imagePath = uri.path // Store the image path

                            }

                        },
                        title = "Select Image",
                    )
                }
                item {
                    Cust_Btn(text = "Upload Image", isblue = true) {

                    }
                }
            }
        }
    )
}






