import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.erp.android.ui.graphs.Graph
import com.example.erp.android.ui.screens.CustSplashScreen
import com.example.erp.android.apiServices.ApiViewModel
import com.example.lms.android.Services.Methods
import com.example.erp.android.ui.screens.auth.Auth_Main
import com.example.lms.android.ui.Screens.BottomNavScreens.MainScreen

@SuppressLint("ComposableDestinationInComposeScope")
@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RootNavGraph(
    rootnavController: NavHostController,
    authNavController: NavHostController,
    mainNavController: NavHostController,
    context: Context,
) {
    val viewModel = ApiViewModel()
    val context= LocalContext.current
    val encodedLogo = Uri.encode("https://picsum.photos/300/300")
    val cName = "1 Click Policy ERP"
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
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
                    CustSplashScreen(rootnavController,
                        animatedVisibilityScope = this)
                }

//                      FOR FIRST Install DETECTION
//                       { if (getfirstInstall(context) == true) {
//                            saveFirstInstall(context)
//                            navController.navigate("App_Info")
//                        } else {
//                            navController.navigate(Graph.BOTTOMBAR)
//                        }
//
//                    }}

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
                    val logo = it.arguments?.getString("cLogo") ?: ""
                    val cName = it.arguments?.getString("cName") ?: ""
                    Auth_Main(
                        rootnavController,
                        authNavController,
                        mainNavController,
                        logo,
                        cName,
                        animatedVisibilityScope = this
                    )
                }
                composable(route = Graph.MAIN) {

                        MainScreen(
                            rootnavController,
                            mainNavController,
                            viewModel,
                            context = context,
                        ){
                            Methods().clearToken(context)
                            rootnavController.navigate("AuthScreen/$encodedLogo/$cName")
                            {
                                popUpTo(rootnavController.graph.startDestinationId) {
                                    inclusive = true // This will also remove the start destination from the back stack
                                }
                            }

                        }

                }
            }
        }
    }
}