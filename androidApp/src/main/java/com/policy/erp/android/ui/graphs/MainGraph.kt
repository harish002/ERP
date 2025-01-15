package com.policy.erp.android.ui.graphs

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.policy.erp.android.apiServices.ApiViewModel
import com.policy.erp.android.ui.bottombarGraph.BottomBarScreen
import com.policy.erp.android.ui.screens.bottomNavScreens.FilterScreen
import com.policy.erp.android.ui.screens.bottomNavScreens.Health.HealthData
import com.policy.erp.android.ui.screens.bottomNavScreens.Health.HealthView
import com.policy.erp.android.ui.screens.bottomNavScreens.Health_Rate_Details
import com.policy.erp.android.ui.screens.bottomNavScreens.Life.LifeData
import com.policy.erp.android.ui.screens.bottomNavScreens.Life.LifeView
import com.policy.erp.android.ui.screens.bottomNavScreens.Motor.VehicleData
import com.policy.erp.android.ui.screens.bottomNavScreens.Notification
import com.policy.erp.android.ui.screens.bottomNavScreens.Setting.Network.Network
import com.policy.erp.android.ui.screens.bottomNavScreens.Setting.Profile.Profile
import com.policy.erp.android.ui.screens.bottomNavScreens.Setting.Setting
import com.policy.erp.android.ui.screens.bottomNavScreens.Motor.VehicleNumber
import com.policy.erp.android.ui.screens.bottomNavScreens.Poicy_Rate_Details
import com.policy.erp.android.ui.screens.bottomNavScreens.SME.SMEData
import com.policy.erp.android.ui.screens.bottomNavScreens.SME.SMEView
import com.policy.erp.android.ui.screens.bottomNavScreens.Setting.Network.HealthCashless
import com.policy.erp.android.ui.screens.bottomNavScreens.Setting.Network.MotorCashless


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
                FilterScreen(mainNavController, viewModel, logout)
            }
        }
        BottomBarScreen.Network.route?.let { it1 ->
            composable(route = it1) {
                Network(mainNavController)
            }
        }
        BottomBarScreen.Vehicle_Data.route?.let { it1 ->
            composable(route = it1) {
                VehicleData(viewModel, mainNavController)
            }
        }
        BottomBarScreen.Health_Data.route?.let { it1 ->
            composable(route = it1) {
                HealthData(viewModel, mainNavController)
            }
        }
        BottomBarScreen.Life_Data.route?.let { it1 ->
            composable(route = it1) {
                LifeData(viewModel, mainNavController)
            }
        }
        BottomBarScreen.SME_Data.route?.let { it1 ->
            composable(route = it1) {
                SMEData(viewModel, mainNavController)
            }
        }
        BottomBarScreen.Poicy_Rate_Details.route?.let { it1 ->
            composable(route = "$it1/{policyDataId}",
                arguments = listOf(
                    navArgument("policyDataId") {
                        type = NavType.StringType
                    }
                )) { i ->
                val data = i.arguments?.getString("policyDataId") ?: ""
                Poicy_Rate_Details(mainNavController, data = data, viewModel)
            }
        }
        BottomBarScreen.Health_Rate_Details.route?.let { it1 ->
            composable(route = "$it1/{policyDataId}",
                arguments = listOf(
                    navArgument("policyDataId") {
                        type = NavType.StringType
                    }
                )) { i ->
                val data = i.arguments?.getString("policyDataId") ?: ""
                Health_Rate_Details(mainNavController, data = data, viewModel)
            }
        }
        BottomBarScreen.Vehicle_Number.route?.let { it1 ->
            composable(route = it1) {
                VehicleNumber(context, viewModel, mainNavController)
            }
        }

        BottomBarScreen.Setting.route?.let { it1 ->
            composable(route = it1) {
                Setting(mainNavController, context, logout)
            }
        }

        BottomBarScreen.Notification.route?.let { it1 ->
            composable(route = it1) {
                Notification(mainNavController, viewModel, context)
            }
        }
        BottomBarScreen.Health.route?.let { it1 ->
            composable(route = it1) {
                HealthView(context, viewModel, mainNavController)
            }
        }
        BottomBarScreen.Life.route?.let { it1 ->
            composable(route = it1) {
                LifeView(context, viewModel, mainNavController)
            }
        }
        BottomBarScreen.SME.route?.let { it1 ->
            composable(route = it1) {
                SMEView(context, viewModel, mainNavController)
            }
        }
        BottomBarScreen.Profile.route?.let { it1 ->
            composable(route = it1) {
                Profile(mainNavController, context, viewModel, rootnavController, logout)
            }
        }
        BottomBarScreen.HealthCashless.route?.let { it1 ->
            composable(route = it1) {
                HealthCashless(mainNavController, context)
            }
        }
        BottomBarScreen.MotorCashless.route?.let { it1 ->
            composable(route = it1) {
                MotorCashless(mainNavController, context)
            }
        }
    }
}

