package com.example.erp.android

import RootNavGraph
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.erp.Greeting

class MainActivity : ComponentActivity() {
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


