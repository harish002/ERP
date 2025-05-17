package com.policy.erp.android.ui.bottombarGraph

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.policy.erp.android.ERPTheme
import com.policy.erp.android.R
import com.policy.erp.android.apiServices.ApiViewModel


@Composable
fun BottomNav(navController: NavController, context: Context) {
    ERPTheme {
        val navBackStackEntry by
        navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination

        Row(
            horizontalArrangement = Arrangement.Absolute.SpaceAround,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 2.dp,
                    bottom = 12.dp
                )
                .background(Color.Transparent)
        ) {

            Product_Screens.forEach { screen ->
                AddItem(
                    context,
                    screen = screen,
                    currentDestination = currentRoute,
                    navController = navController
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.AddItem(
    context: Context,
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val contentColor =
        if (selected) MaterialTheme.colorScheme.primary
        else Color(0xFF6C757D)

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .size(68.dp)
            .align(Alignment.CenterVertically)
            .clickable(onClick = {
                if (screen.route == BottomBarScreen.HelpLine.route) {
                    Log.d("BottomNav", "HelpLine clicked")

                    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:8055875587") // Replace with your desired number
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    // Check if the dialer activity is available
                    if (dialIntent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(dialIntent) // Start the dialer
                    } else {
                        // If no dialer is available, show an error toast
                        Toast
                            .makeText(context, "No dialer found else", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else if (screen.route == BottomBarScreen.Vehicle_Number.route) {
                    showBottomSheet = !showBottomSheet
                } else {
                    screen.route?.let {
                        navController.navigate(it) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true

                            restoreState = true

                        }
                    }
                }
            }),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 4.dp)
            ) {


                screen.icon?.let { painterResource(id = it) }?.let {
                    Image(
                        painter = it,
                        colorFilter =
                        if (screen.route == BottomBarScreen.Vehicle_Number.route) {
                            ColorFilter.tint(MaterialTheme.colorScheme.scrim)
                        } else if (screen.route == BottomBarScreen.HelpLine.route) {
                            ColorFilter.tint(Color(0xFF04C98B))
                        } else {
                            ColorFilter.tint(
                                if (!selected) {
                                    Color(0xFF6C757D)
                                } else MaterialTheme.colorScheme.primary
                            )
                        },
                        contentDescription = "icon",
                        modifier = if (screen.route == BottomBarScreen.Vehicle_Number.route) {
                            Modifier
                                .size(60.dp)
                                .clip(shape = RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(18.dp)
                        } else {
                            Modifier
                                .size(42.dp)
                                .padding(10.dp)
                        },
                    )
                }


                screen.title?.let {
                    if (screen.route == BottomBarScreen.Vehicle_Number.route) {
                    } else {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (screen.route == BottomBarScreen.HelpLine.route) {
                                Color(0xFF04C98B)
                            } else {
                                contentColor
                            }
                        )
                    }
                }
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth(),
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            dragHandle = {},
            containerColor = MaterialTheme.colorScheme.background
        ) {

            PolicySegmentsView(ApiViewModel(),navController){
                showBottomSheet = !showBottomSheet
            }

        }
    }
}


sealed class BottomBarScreen(
    val route: String?,
    val title: String?,
    val icon: Int?,
//    val icon_focused: ImageVector?
) {

    // for home
    data object Policy_Rate : BottomBarScreen(
        route = "Policy_Rate",
        title = "Policy Rate",
        icon = R.drawable.policy_rate,
//        icon_focused = Icons.Outlined.Search
    )


    // Search
    data object Vehicle_Number : BottomBarScreen(
        route = "Vehicle Number",
        title = "Vehicle Number",
        icon = R.drawable.bottom_icon,
    )

    // PoicyRateDetails
    data object Poicy_Rate_Details : BottomBarScreen(
        route = "PoicyRateDetails/{policyDataId}",
        title = "PoicyRateDetails",
        icon = R.drawable.camera,
    )

    // PoicyRateDetails
    data object Health_Rate_Details : BottomBarScreen(
        route = "HealthRateDetails/{policyDataId}",
        title = "HealthRateDetails",
        icon = R.drawable.camera,
    )

    //     Network Screen
    data object Network : BottomBarScreen(
        route = "Network",
        title = "Network",
        icon = R.drawable.lock_01,
    )

    //     Filter Vehicel Data
    data object Vehicle_Data : BottomBarScreen(
        route = "Vehicle_Data",
        title = "Vehicle_Data",
        icon = R.drawable.lock_01,
    )

    //     HealthData Vehicel Data
    data object Health_Data : BottomBarScreen(
        route = "Health_Data",
        title = "Health_Data",
        icon = R.drawable.lock_01,
    )
//    SME  Data
    data object SME_Data : BottomBarScreen(
        route = "SME_Data",
        title = "SME_Data",
        icon = R.drawable.lock_01,
    )
//    Life Data
    data object Life_Data : BottomBarScreen(
        route = "Life_Data",
        title = "Life_Data",
        icon = R.drawable.lock_01,
    )

    // for User Setting
    data object Setting : BottomBarScreen(
        route = "Setting",
        title = "Setting",
        icon = R.drawable.profile,
//        icon_focused = Icons.Outlined.AccountCircle
    )

    // for User Setting
    data object Profile : BottomBarScreen(
        route = "Profile",
        title = "Profile",
        icon = R.drawable.profile,
//        icon_focused = Icons.Outlined.AccountCircle
    )
    // for User Security
    data object Security  : BottomBarScreen(
        route = "security",
        title = "security",
        icon = R.drawable.security,
//        icon_focused = Icons.Outlined.AccountCircle
    )

    // for All Categories
    data object Notification : BottomBarScreen(
        route = "Notification",
        title = "Notification",
        icon = R.drawable.notification,
//        icon_focused = Icons.Outlined.AccountCircle
    )    // for All Categories

    data object HelpLine : BottomBarScreen(
        route = "Help_Line",
        title = "Help Line",
        icon = R.drawable.support_call,
//        icon_focused = Icons.Outlined.AccountCircle
    )
    data object Health : BottomBarScreen(
        route = "Health",
        title = "Health",
        icon = R.drawable.support_call,
//        icon_focused = Icons.Outlined.AccountCircle
    )
    data object Life : BottomBarScreen(
        route = "Life",
        title = "Life",
        icon = R.drawable.support_call,
//        icon_focused = Icons.Outlined.AccountCircle
    )

    data object SME : BottomBarScreen(
        route = "SME",
        title = "SME",
        icon = R.drawable.support_call,
//        icon_focused = Icons.Outlined.AccountCircle
    )


    data object HealthCashless : BottomBarScreen(
        route = "HealthCashless",
        title = "HealthCashless",
        icon = R.drawable.support_call,
//        icon_focused = Icons.Outlined.AccountCircle
    )


    data object MotorCashless : BottomBarScreen(
        route = "MotorCashless",
        title = "MotorCashless",
        icon = R.drawable.support_call,
//        icon_focused = Icons.Outlined.AccountCircle
    )


}

//navigation

val Product_Screens = listOf(
    BottomBarScreen.Policy_Rate,
    BottomBarScreen.Setting,
    BottomBarScreen.Vehicle_Number,
    BottomBarScreen.Notification,
    BottomBarScreen.HelpLine
//    BottomBarScreen.Progress,
)


@Composable
fun PolicySegmentsView(
    accessModel: ApiViewModel = viewModel(),
    mainNavController: NavController,
    onDismiss: () -> Unit
) {
    val segmentsList = listOf(
        Segment("Motor", R.drawable.motor),
        Segment("Health", R.drawable.health),
        Segment("Life", R.drawable.life),
        Segment("SME", R.drawable.sme)
    )

    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Header(onDismiss)

        LazyVerticalGrid(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .padding(8.dp),
            columns = GridCells.Fixed(2), // 3 columns
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(segmentsList) { segment ->
                SegmentButton(segment) {
                    when (segment.name) {
                        "Motor" -> {
                            onDismiss()
                            BottomBarScreen.Vehicle_Number.route?.let {
                                mainNavController.navigate(
                                    it
                                )
                            }
                        }

                        "Health" -> {
                             onDismiss()
                            BottomBarScreen.Health.route?.let {
                                mainNavController.navigate(
                                    it
                                )
                            }
                        }

                        "Life" -> {
                             onDismiss()
                            BottomBarScreen.Life.route?.let {
                                mainNavController.navigate(
                                    it
                                )
                            }
                        }

                        "SME" -> {
                            BottomBarScreen.SME.route?.let {
                                mainNavController.navigate(
                                    it
                                )
                            }
                        }

                        else -> {
                            println("No Such Page Found")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Header(isSheetClosed: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Policy Segments",
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Image(
            painter = painterResource(id = R.drawable.close_button),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    isSheetClosed()
                }
        )
    }
}

@Composable
fun SegmentButton(segment: Segment, onClick: () -> Unit) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer,
        ),
        start = Offset(0f, 90f),
        end = Offset(0f, 1800f)
    )
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(120.dp)
            .clickable(onClick = onClick)
            .background(gradient)
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface), shape = RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = segment.iconName),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.padding(4.dp))
            Text(text = segment.name,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp)
        }
    }
}

data class Segment(val name: String, val iconName: Int)

