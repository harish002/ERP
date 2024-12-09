package com.chp.lms.android.ui.Screens.BottomNavScreens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.chp.erp.android.ERPTheme
import com.chp.erp.android.ui.bottombarGraph.BottomBarScreen
import com.chp.erp.android.ui.bottombarGraph.BottomNav
import com.chp.erp.android.ui.graphs.MainNavGraph
import com.chp.erp.android.ui.graphs.allRoutes
import com.chp.erp.android.apiServices.ApiViewModel

@SuppressLint("SuspiciousIndentation")
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



    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF1f2add),
            Color(0xFF00de81),
        ), // Customize your colors here
        start = Offset(40f, 0f),
        end = Offset(1400f, 0f) // Adjust the end point for gradient direction
    )
    // Check if the current route is BottomBarScreen.Fab.route
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    val bottomBarDestination = (allRoutes.contains(currentRoute?.route))
    ERPTheme {
        Scaffold(
            bottomBar = {
                if (bottomBarDestination) {
                    Box(
                    ) { BottomNav(navController = mainNavController, context) }
                }
            },
            floatingActionButton = {

            }
        ) { paddingValues ->
            Box(
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
                )
                {
                    logout()
                }
            }

        }
    }
}

