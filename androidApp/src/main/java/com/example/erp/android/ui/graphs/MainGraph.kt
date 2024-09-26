package com.example.erp.android.ui.graphs

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.erp.android.ui.bottombarGraph.BottomBarScreen
import com.example.erp.android.ui.screens.bottomNavScreens.FilterScreen
import com.example.erp.android.ui.screens.bottomNavScreens.Profile
import com.example.erp.android.ui.screens.bottomNavScreens.VehicleNumber
import com.example.erp.android.apiServices.ApiViewModel


val allRoutes = mutableListOf(
    BottomBarScreen.Explore.route.toString(),
    BottomBarScreen.Learn.route.toString(),
    BottomBarScreen.Search.route.toString(),
    BottomBarScreen.Progress.route.toString(),
    BottomBarScreen.Profile.route.toString(),
    BottomBarScreen.AllCategory.route.toString(),
    BottomBarScreen.specificCategory.route.toString(),

    )

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainNavGraph(
    rootnavController: NavHostController,
    mainNavController: NavHostController,
    context: Context,
    viewModel: ApiViewModel,
//  paddingValues: PaddingValues,
    onBottomNavigationStateChanged: (Boolean) -> Unit,
    logout: () -> Unit
) {
    val exploreNavController = rememberNavController()

    NavHost(
        navController = mainNavController,
        route = Graph.MAIN,
//        modifier = Modifier.padding(paddingValues),
        startDestination = BottomBarScreen.Explore.route.toString()
    ) {
        BottomBarScreen.Explore.route?.let { it1 ->
            composable(it1) {
                FilterScreen(viewModel)
            }
        }

        BottomBarScreen.Learn.route?.let { it1 ->
            composable(route = it1) {
                Text("Learn()")
            }
        }
        BottomBarScreen.Search.route?.let { it1 ->
            composable(route = it1) {
                VehicleNumber(context,viewModel)
            }
        }
        BottomBarScreen.Progress.route?.let { it1 ->
            composable(route = it1) {
               Text("Progress(mainNavController, viewModel, rootnavController, exploreNavController)")
            }
        }
        BottomBarScreen.Profile.route?.let { it1 ->
            composable(route = it1) {
                Profile(mainNavController, context,viewModel, rootnavController, { logout() })
            }
        }
        BottomBarScreen.AllCategory.route?.let {
            composable(route = it) {
                "AllCategory(mainNavController, viewModel, rootnavController)"
            }
        }
    }

}

