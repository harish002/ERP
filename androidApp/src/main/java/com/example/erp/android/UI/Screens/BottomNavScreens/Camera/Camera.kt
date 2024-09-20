package com.example.erp.android.UI.Screens.BottomNavScreens.Camera;

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class CameraHandler(private val activity: AppCompatActivity) {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var photoUri: Uri? = null

    // State variables to manage the UI state
    var shouldShowCamera = mutableStateOf(false)
    var shouldShowPhoto = mutableStateOf(false)

    init {
        // Initialize the permission launcher
        requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("kilo", "Camera permission granted")
                shouldShowCamera.value = true
            } else {
                Log.i("kilo", "Camera permission denied")
            }
        }
    }

    fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("kilo", "Permission previously granted")
                shouldShowCamera.value = true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            ) -> {
                Log.i("kilo", "Show camera permissions dialog")
                // Show rationale dialog to the user here if needed.
            }

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun handleImageCapture(uri: Uri) {
        Log.i("kilo", "Image captured: $uri")
        shouldShowCamera.value = false

        photoUri = uri
        shouldShowPhoto.value = true

        // Additional logic for handling the captured image can be added here.
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity.externalMediaDirs.firstOrNull()?.let {
            File(it, "1 Click Policy ERP").apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else activity.filesDir
    }

    fun onDestroy() {
        // Clean up resources if needed when the activity is destroyed.
        // For example, shutting down executors or freeing up resources.
    }
}
