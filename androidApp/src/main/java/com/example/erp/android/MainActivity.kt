package com.example.erp.android

import RootNavGraph
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import java.io.File
import java.util.concurrent.ExecutorService

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)

    lateinit var outputDirectory: File
    lateinit var cameraExecutor: ExecutorService

    var shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)
    lateinit var photoUri: Uri
    var shouldShowPhoto: MutableState<Boolean> = mutableStateOf(false)

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i("ERP camera permission", "Permission granted")
            shouldShowCamera.value = true
        } else {
            Log.i("ERP Camera permission", "Permission denied")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val rootnavController = rememberNavController()
            val authNavController = rememberNavController()
            val mainNavController = rememberNavController()
            ERPTheme {
                RootNavGraph(
                    rootnavController,
                    authNavController,
                    mainNavController,
                    LocalContext.current,
                )
            }
        }
    }


}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}


