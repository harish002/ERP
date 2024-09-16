package com.example.erp.android.UI.Graphs

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.erp.android.UI.BottomBarGraph.BottomBarScreen
import com.example.erp.android.UI.Screens.BottomNavScreens.FilterScreen
import com.example.erp.android.UI.Screens.BottomNavScreens.Profile
import com.example.lms.android.Services.ApiViewModel


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
                FilterScreen()
            }
        }

        BottomBarScreen.Learn.route?.let { it1 ->
            composable(route = it1) {
                Text("Learn()")
            }
        }
        BottomBarScreen.Search.route?.let { it1 ->
            composable(route = it1) {
                Text("Search(mainNavController, viewModel, rootnavController, exploreNavController)")
            }
        }
        BottomBarScreen.Progress.route?.let { it1 ->
            composable(route = it1) {
               Text("Progress(mainNavController, viewModel, rootnavController, exploreNavController)")
            }
        }
        BottomBarScreen.Profile.route?.let { it1 ->
            composable(route = it1) {
                Profile(mainNavController, context, rootnavController, { logout() })
            }
        }
        BottomBarScreen.AllCategory.route?.let {
            composable(route = it) {
                "AllCategory(mainNavController, viewModel, rootnavController)"
            }
        }
    }

}

