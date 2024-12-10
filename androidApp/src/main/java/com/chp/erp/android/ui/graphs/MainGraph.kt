package com.chp.erp.android.ui.graphs

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.chp.erp.android.apiServices.ApiViewModel
import com.chp.erp.android.ui.bottombarGraph.BottomBarScreen
import com.chp.erp.android.ui.screens.bottomNavScreens.FilterScreen
import com.chp.erp.android.ui.screens.bottomNavScreens.Notification
import com.chp.erp.android.ui.screens.bottomNavScreens.Profile.Network
import com.chp.erp.android.ui.screens.bottomNavScreens.Profile.Profile
import com.chp.erp.android.ui.screens.bottomNavScreens.Setting
import com.chp.erp.android.ui.screens.bottomNavScreens.Motor.VehicleNumber
import com.chp.erp.android.ui.screens.bottomNavScreens.Poicy_Rate_Details


val allRoutes = mutableListOf(
    BottomBarScreen.Policy_Rate.route.toString(),
//    BottomBarScreen.Vehicle_Number.route.toString(),
    BottomBarScreen.Setting.route.toString(),
    BottomBarScreen.HelpLine.route.toString(),
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

    NavHost(
        navController = mainNavController,
        route = Graph.MAIN,
//        modifier = Modifier.padding(paddingValues),
        startDestination = BottomBarScreen.Policy_Rate.route.toString()
    ) {
        BottomBarScreen.Policy_Rate.route?.let { it1 ->
            composable(it1) {
                FilterScreen(mainNavController,viewModel, logout)
            }
        }
        BottomBarScreen.Network.route?.let { it1 ->
            composable(route = it1) {
                Network(mainNavController)
            }
        }
        BottomBarScreen.Poicy_Rate_Details.route?.let { it1 ->
            composable(route = "$it1/{policyDataId}",
                arguments = listOf(
                    navArgument("policyDataId") {
                        type = NavType.StringType
                    }
                )) {
                    i ->
                val data = i.arguments?.getString("policyDataId") ?: ""
                Poicy_Rate_Details(mainNavController, data = data,viewModel)
            }
        }
        BottomBarScreen.Vehicle_Number.route?.let { it1 ->
            composable(route = it1) {
                VehicleNumber(context, viewModel,mainNavController)
            }
        }

        BottomBarScreen.Setting.route?.let { it1 ->
            composable(route = it1) {
                Setting(mainNavController,context,logout)
            }
        }
        BottomBarScreen.Notification.route?.let { it1 ->
            composable(route = it1) {
                Notification(mainNavController,viewModel,context)
            }
        }
        BottomBarScreen.Profile.route?.let { it1 ->
            composable(route = it1) {
                Profile(mainNavController,context,viewModel,rootnavController,logout)
            }
        }

    }
}

