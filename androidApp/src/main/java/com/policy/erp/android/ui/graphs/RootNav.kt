import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.policy.erp.android.ui.graphs.Graph
import com.policy.erp.android.ui.screens.CustSplashScreen
import com.policy.erp.android.apiServices.ApiViewModel
import com.policy.erp.android.apiServices.project_id
import com.policy.lms.android.Services.Methods
import com.policy.erp.android.ui.screens.auth.Auth_Main
import com.policy.lms.android.Services.getfirstInstall
import com.policy.lms.android.Services.saveFirstInstall
import com.policy.lms.android.ui.Screens.BottomNavScreens.MainScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("ComposableDestinationInComposeScope", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RootNavGraph(
    context: Context,
) {
    val rootnavController = rememberNavController()

    val viewModel = ApiViewModel()
    val context= LocalContext.current
    val encodedLogo = Uri.encode("https://hellopolicy.com/images/1clickpolicy_logo.svg")
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF1F2ADC),
            Color(0xFF04C98B),
        ),
        start = Offset(0f, 90f),
        end = Offset(0f, 1800f)
    )
    Box(modifier = Modifier.fillMaxSize()
        .background(gradient)
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            SharedTransitionLayout {
                NavHost(
                    navController = rootnavController,
                    route = Graph.ROOT,
                    startDestination =
//                Graph.MAIN
                    "Splashscreen",
                ) {

                    composable(route = "Splashscreen") {
                        CustSplashScreen(
                            rootnavController,
                            animatedVisibilityScope = this
                        )
//                }

////                      FOR FIRST Install DETECTION
////                       {
//                        if (getfirstInstall(context) == true) {
//                            saveFirstInstall(context)
//                            CoroutineScope(Dispatchers.IO).launch {
//                                Methods().retrieve_DToken(context)?.let {
//                                    Methods().retrieve_Token(context)?.let { it1 ->
//                                        Methods().retrieve_userID(context)?.let { it2 ->
//                                            viewModel.registerDeviceForNotification(
//                                                token = it1,
//                                                projectId = project_id,
//                                                userId = it2,
//                                                deviceToken = it
//                                            )
//                                        }
//
//                                    }
//                                }
//                            }
//                        }
                    }


                    composable(

                        route = "AuthScreen/{cLogo}/{cName}",
                        arguments = listOf(
                            navArgument("cLogo") {
                                type = NavType.StringType
                            },
                            navArgument("cName") {
                                type = NavType.StringType
                            },
                        ),
                    ) {
                        val authNavController = rememberNavController()
                        val logo = it.arguments?.getString("cLogo") ?: ""
                        val cName = it.arguments?.getString("cName") ?: ""
                        Auth_Main(
                            rootnavController,
                            authNavController,
                            logo,
                            cName,
                            animatedVisibilityScope = this
                        )
                    }
                    composable(route = Graph.MAIN) {
                        val mainNavController = rememberNavController()

                        MainScreen(
                            rootnavController,
                            mainNavController,
                            viewModel,
                            context = context,
                        ) {
                            Methods().clearToken(context)
                            rootnavController.navigate(Graph.ROOT)
                            {

                                popUpTo(Graph.ROOT) { inclusive = true }
                                launchSingleTop = true
                            }

                        }

                    }
                }
            }
        }
    }
}