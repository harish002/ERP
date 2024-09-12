package com.example.erp.android.UI.Graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.lms.android.Services.ApiViewModel
import com.example.lms.android.ui.Screens.Auth.Login.AccNot_Found
import com.example.lms.android.ui.Screens.Auth.Login.Get_OTP
import com.example.lms.android.ui.Screens.Auth.Login.Login_Screen
import com.example.lms.android.ui.Screens.Auth.Login.OtpSent_Screen
import com.example.lms.android.ui.Screens.Auth.Login.Reset_Password
import com.example.lms.android.ui.Screens.Auth.Otp_Screen
import kotlinx.coroutines.CoroutineScope


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AuthNav_Graph(
    navController: NavHostController,
    authNavController: NavHostController,
    sheetState: SheetState,
    scope: CoroutineScope,
    onDismiss: () -> Unit,
    route: String
) {
    val viewModel = ApiViewModel()
    val context = LocalContext.current
    NavHost(
        navController = authNavController,
        route = Graph.AUTH,
        startDestination = route
    )
    {
        composable(route = AuthScreen.Login.route) {
            Login_Screen(
                navController=navController,
                authnavController=authNavController, sheetState = sheetState,
                scope = scope, viewModel = viewModel
            ) { onDismiss() }
        }
//        composable(route = AuthScreen.SignUp.route) {
//            SignupScreen(
//                navController = authNavController, sheetState, scope = scope,
//                viewModel = viewModel
//            )
//            { onDismiss() }
//        }
        composable(
            route = "${AuthScreen.Otp.route}/{screentype}/{otptype}/{phone}",
            arguments = listOf(
                navArgument("screentype"){
                    type = NavType.StringType
                },
                navArgument("otptype"){
                    type = NavType.StringType
                },
                navArgument("phone"){
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val screentype = backStackEntry.arguments?.getString("screentype") ?: ""
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val otptype = backStackEntry.arguments?.getString("otptype") ?: ""
            Otp_Screen(
                mainNavController=navController,
                authnavController = authNavController,
                sheetState, scope, viewModel,
                otptype, screentype,phone
            ) {
                onDismiss()
            }
        }
        composable(
            route = "${AuthScreen.Get_OTP.route}/{otptype}",
            arguments = listOf(navArgument("otptype") { type = NavType.StringType })
        ) { backStackEntry ->
            val otptype = backStackEntry.arguments?.getString("otptype") ?: ""

            Get_OTP(navController = authNavController, sheetState, scope,viewModel, otptype) {
                onDismiss()
            }
        }
        composable(route = "${AuthScreen.OtpSent.route}/{otptype}/{phone}",
            arguments = listOf(
                navArgument("otptype") {
                    type = NavType.StringType
                },
                navArgument("phone") {
                    type = NavType.StringType
                }
            )) { backStackEntry ->
            val otptype = backStackEntry.arguments?.getString("otptype") ?: ""
            val phone = backStackEntry.arguments?.getString("phone") ?: ""

            OtpSent_Screen(navController = authNavController, sheetState, otptype, scope,phone)
            { onDismiss() }
        }
        composable(route = AuthScreen.AccNot_Found.route) {
            AccNot_Found(navController = authNavController, sheetState, scope)
            { onDismiss() }
        }
        composable(route = AuthScreen.Reset_Password.route) {
            Reset_Password(navController = authNavController, sheetState, scope)
            { onDismiss() }
        }
//        composable(route = "${AuthScreen.Verify_Account.route}/{verifyType}",
//            arguments = listOf(
//                //as the Verify page is common in Register and Login Flow
//                navArgument("verifyType") {
//                    type = NavType.StringType
//                }
//            )) {backStackEntry ->
//            val verifyType = backStackEntry.arguments?.getString("verifyType") ?: ""
//
//            Verify_Account(
//                navController = authNavController,
//                sheetState = sheetState,
//                scope = scope,
//                viewModel = viewModel,
//                verifyType=verifyType
//            ) {
//
//            }
//        }

    }
}


sealed class AuthScreen(val route: String) {

    object Login : AuthScreen(route = "login")
    object SignUp : AuthScreen(route = "signUp")
    object Reset_Password : AuthScreen(route = "Reset_Password")

    object OtpSent : AuthScreen(route = "OtpSent_Screen/{otp type}/{phone}")
    object AccNot_Found : AuthScreen(route = "accountabilityScreen")
    object Verify_Account : AuthScreen(route = "Verify_Account/{verifyType}")
    object Get_OTP : AuthScreen(route = "Get_Otp_Screen/{otptype}")

    object Otp : AuthScreen(route = "Otp_Screen/{screentype}/{otptype}/{phone}")

}
