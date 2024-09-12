package com.example.erp.android.UI.BottomBarGraph

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.erp.android.ERPTheme
import com.example.erp.android.R

@Composable
fun BottomNav(navController: NavController) {
    ERPTheme{
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
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val contentColor =
        if (selected) MaterialTheme.colorScheme.primary else Color(0xFF949494)

    Box(
        modifier = Modifier
            .size(61.dp)
            .align(Alignment.CenterVertically)
            .clickable(onClick = {
                screen.route?.let {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true

                        restoreState = true

                    }
                }
            }),

        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(2.dp)
            ) {


                    screen.icon?.let { painterResource(id = it) }?.let {
                        Icon(
                            painter = it,
                            contentDescription = "icon",
                            tint = contentColor
                        )
                    }

                    Spacer(modifier = Modifier.padding(vertical = 4.dp))

                    screen.title?.let {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            color = contentColor
                        )
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
    data object Explore: BottomBarScreen(
        route = "Explore",
        title = "Explore",
        icon =  R.drawable.lock_01,
//        icon_focused = Icons.Outlined.Search
    )
    // for Enrolled Course
    data object EnrolledCourse: BottomBarScreen(
        route = "Enrolled Course/{courseID}",
        title = "Enrolled Course",
        icon =   R.drawable.lock_01,
//        icon_focused = Icons.Outlined.Search
    )

    data object Progress: BottomBarScreen(
        route = "Progress",
        title = "Progress",
        icon =   R.drawable.lock_01,
//        icon =  com.example.lms.R.drawable.award_05,

//        icon_focused = Icons.Outlined.Search
    )

    // All Courses
    data object Learn: BottomBarScreen(
        route = "Learn",
        title = "Learn",
        icon =  R.drawable.lock_01,
//        icon_focused = Icons.Outlined.Face
    )
    // Search
    data object Search: BottomBarScreen(
        route = "Search",
        title = "Search",
        icon =  R.drawable.lock_01,
    )

    // NotificationScreen
    data object NotificationScreen: BottomBarScreen(
        route = "NotificationScreen",
        title = "NotificationScreen",
        icon =  R.drawable.lock_01,
    )

    // for User Profile
    data object Profile: BottomBarScreen(
        route = "profile",
        title = "Profile",
        icon =  R.drawable.lock_01,
//        icon_focused = Icons.Outlined.AccountCircle
    )

    // for All Categories
    data object AllCategory: BottomBarScreen(
        route = "all_category",
        title = "all_category",
        icon =  R.drawable.lock_01,
//        icon_focused = Icons.Outlined.AccountCircle
    )
    // for single Category
    data object specificCategory: BottomBarScreen(
        route = "specific_category/{categoryID}/{categoryName}",
        title = "specific_category",
        icon =  R.drawable.lock_01,
//        icon_focused = Icons.Outlined.AccountCircle
    )

    // for All Categories
    data object CourseDetails: BottomBarScreen(
        route = "course_Details",
        title = "course_Details",
        icon =  R.drawable.lock_01,
//        icon_focused = Icons.Outlined.AccountCircle
    )

}

//navigation

val Product_Screens = listOf(
    BottomBarScreen.Explore,
    BottomBarScreen.Search,
//    BottomBarScreen.Learn,
    BottomBarScreen.Progress,
    BottomBarScreen.Profile
)
