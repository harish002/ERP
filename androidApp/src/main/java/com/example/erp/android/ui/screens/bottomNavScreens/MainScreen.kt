package com.example.lms.android.ui.Screens.BottomNavScreens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.erp.android.ERPTheme
import com.example.erp.android.ui.bottombarGraph.BottomBarScreen
import com.example.erp.android.ui.bottombarGraph.BottomNav
import com.example.erp.android.ui.graphs.MainNavGraph
import com.example.erp.android.ui.graphs.allRoutes
import com.example.erp.android.apiServices.ApiViewModel

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainScreen(
    rootnavController: NavHostController,
    mainNavController: NavHostController,
    viewModel: ApiViewModel,
    context: Context,
    logout:()->Unit
) {

    var isBottomNavigationEnabled by remember { mutableStateOf(true) }

    val routeToTitleMap = mapOf(
        BottomBarScreen.Explore.route to "Explore",
        BottomBarScreen.Learn.route to "Learn",
        BottomBarScreen.Progress.route to "Progress",
        BottomBarScreen.Profile.route to "Profile",
    )

    // Check if the current route is BottomBarScreen.Fab.route
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    val bottomBarDestination = (allRoutes.contains(currentRoute?.route))
    ERPTheme {
        Scaffold(
            bottomBar = {
                if (bottomBarDestination) {
                    BottomNav(navController = mainNavController)
                }
            },
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

