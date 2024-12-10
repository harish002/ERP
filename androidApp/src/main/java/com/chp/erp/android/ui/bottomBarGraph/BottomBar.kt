package com.chp.erp.android.ui.bottombarGraph

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.chp.erp.android.ERPTheme
import com.chp.erp.android.R

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
                            ColorFilter.tint(MaterialTheme.colorScheme.background)
                        }
                        else if (screen.route == BottomBarScreen.HelpLine.route) {
                           ColorFilter.tint( Color(0xFF04C98B))
                        }
                        else {
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
        icon = R.drawable.camera,
    )

    // PoicyRateDetails
    data object Poicy_Rate_Details : BottomBarScreen(
        route = "PoicyRateDetails/{data}",
        title = "PoicyRateDetails",
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
