package com.example.erp.android.UI.Screens.BottomNavScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.erp.android.ERPTheme
import com.example.erp.android.UI.Screens.PolicyListView
import com.example.lms.android.Services.Methods
import com.example.lms.android.ui.Component.Selection_View

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen() {
    val context = LocalContext.current

    var selectedValue by remember { mutableStateOf("") }
    val optionValue = remember { mutableStateListOf("1", "2", "3") }

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    var searchVal by remember { mutableStateOf("") }
    val colors: TextFieldColors = TextFieldDefaults.colors(
        cursorColor = Color.Black,
        disabledLabelColor = Color(0xFF949494),
        focusedLabelColor = Color(0xFF4E4E4E),
        unfocusedTextColor = Color(0xFF949494),
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = MaterialTheme.colorScheme.background,
        unfocusedIndicatorColor = Color(0xFFD0D0D0)
    )

    var loading by remember { mutableStateOf(true) }

    ERPTheme {
        LaunchedEffect(context) {
            Methods().retrieve_Token(context)?.let {
                loading = false
            }

        }
//        val allCategorylist = viewModel.allCourseCate.collectAsState()
//        val allPublishlist = viewModel.allPublishesCourses.collectAsState()
        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onBackground)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    title = { Text("Sales Tools") },
                    actions = {
                        // Add the navigation icon here
                        Box(
                            modifier = Modifier
                                .wrapContentSize(),
                            // Adjust padding as needed
                            contentAlignment = Alignment.CenterEnd
                        ) {
//                            NavigationIcon(navController = mainNavController)
                        }
                    },
                    navigationIcon = {
                        if (scrollBehavior.state.collapsedFraction < 1f) {
                            CircularProfileWithWelcome("Test User")
                        }
//                        CircularProfileWithWelcome("Test User")
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        scrolledContainerColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = Color.Black,
                        titleContentColor = Color.Black,
                        actionIconContentColor = Color.LightGray,
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(MaterialTheme.colorScheme.onBackground)
            ) {

                items(7) {
                    PolicyListView()
                }

                item{
                    com.example.erp.android.UI.Screens.Selection_View(
                        selectedValue= selectedValue,
                        options = optionValue,
                        label = "Filter"
                    ) {

                    }
                }

            }
        }
    }
}


@Composable
fun CircularProfileWithWelcome(userName: String) {
    val initials = userName.split(" ").joinToString("") { it.take(1) }.uppercase()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp) // Optional padding for the row
    ) {
        // Circular Profile Icon
        Box(
            modifier = Modifier
                .size(60.dp) // Size of the circular icon
                .background(Color.Gray, shape = CircleShape) // Circular shape with background color
                .border(2.dp, Color.White, shape = CircleShape) // Optional border
                .padding(8.dp), // Padding inside the circle
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text

        // Welcome Message
        Column {
            Text(
                text = "Welcome",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = userName,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}