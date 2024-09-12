import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.erp.android.UI.Graphs.Graph
import com.example.erp.android.UI.Screens.CustSplashScreen
import com.example.lms.android.Services.ApiViewModel
import com.example.lms.android.ui.Screens.Auth.Auth_Main
import com.example.lms.android.ui.Screens.BottomNavScreens.MainScreen

@SuppressLint("ComposableDestinationInComposeScope")
@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RootNavGraph(
    navController: NavHostController,
    authNavController: NavHostController,
    mainNavController: NavHostController,
    context: Context,
) {
    val viewModel = ApiViewModel()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        SharedTransitionLayout {
            NavHost(
                navController = navController,
                route = Graph.ROOT,
                startDestination =
//                Graph.MAIN
                "Splashscreen",
            ) {

                composable(route = "Splashscreen") {
                    CustSplashScreen(navController,
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
                        navController,
                        authNavController,
                        mainNavController,
                        logo,
                        cName,
                        animatedVisibilityScope = this
                    )
                }
                composable(route = Graph.MAIN) {

                        MainScreen(
                            navController,
                            mainNavController,
                            viewModel,
                            context = context
                        )

                }
            }
        }
    }
}