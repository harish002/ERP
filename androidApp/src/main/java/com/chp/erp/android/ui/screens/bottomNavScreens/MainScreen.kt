package com.chp.lms.android.ui.Screens.BottomNavScreens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.chp.erp.android.ERPTheme
import com.chp.erp.android.ui.bottombarGraph.BottomBarScreen
import com.chp.erp.android.ui.bottombarGraph.BottomNav
import com.chp.erp.android.ui.graphs.MainNavGraph
import com.chp.erp.android.ui.graphs.allRoutes
import com.chp.erp.android.apiServices.ApiViewModel
import com.chp.erp.android.ui.screens.GridItem

@OptIn(ExperimentalMaterial3Api::class)@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainScreen(
    rootnavController: NavHostController,
    mainNavController: NavHostController,
    viewModel: ApiViewModel,
    context: Context,
    logout: () -> Unit
) {
    var isBottomNavigationEnabled by remember { mutableStateOf(true) }
    var isModalSheetVisible by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF1f2add),
            Color(0xFF00de81),
        ),
        start = Offset(40f, 0f),
        end = Offset(1400f, 0f)
    )



    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarDestination = allRoutes.contains(currentRoute)

    ERPTheme {
        Scaffold(
            bottomBar = {
                if (bottomBarDestination) {
                    BottomNav(navController = mainNavController, context)
                }
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                MainNavGraph(
                    rootnavController,
                    mainNavController,
                    context,
                    viewModel,
                    onBottomNavigationStateChanged = { isEnabled ->
                        isBottomNavigationEnabled = isEnabled
                    }
                ) {
                    logout()
                }

            }



        }
        if (isModalSheetVisible) {
            ModalBottomSheet(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth(),
                onDismissRequest = {
                    isModalSheetVisible = false
                },
                sheetState = sheetState
            ) {
                Text("Test")
                Text("Test")
                Text("Test")
                Text("Test")
            }
        }
    }
}




@Composable
fun ModalBottomSheetContent(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Modal Bottom Sheet",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onDismiss) {
            Text("Dismiss")
        }
    }
}
