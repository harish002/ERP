package com.chp.erp.android.ui.screens.bottomNavScreens.Setting.Profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.chp.erp.android.ERPTheme
import com.chp.erp.android.R
import com.chp.erp.android.apiServices.ApiViewModel
import com.chp.erp.android.ui.bottombarGraph.BottomBarScreen
import com.chp.lms.Services.Dataclass.GetUserData
import com.chp.lms.Services.Dataclass.UserData
import com.chp.lms.android.Services.Methods
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    mainNavController: NavHostController,
    context: Context,
    viewModel: ApiViewModel,
    rootnavController: NavHostController,
    logout: () -> Unit
) {
    val scrollState = rememberScrollState()
    var loading by remember { mutableStateOf(true) }
    var visible by remember { mutableStateOf(false) }
   val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer,
        ), // Customize your colors here
        start = Offset(100f, 0f),
        end = Offset(700f, 0f) // Adjust the end point for gradient direction
    )


    ERPTheme {
        LaunchedEffect(context) {
            Methods().retrieve_Token(context)?.let {
                Methods().retrieve_userID(context)
                    ?.let { it1 -> viewModel.getUserWhoLoggedIn(it, it1) }
                loading = false
            }
        }
        val userData by viewModel.getUserdata.collectAsState()

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.scrim)
                ) {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            Icon(Icons.Outlined.ArrowBack,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "Back Btn Icon",
                                modifier = Modifier
                                    .padding(6.dp)
                                    .clickable { mainNavController.navigateUp() }
                            )
                        },
                        title = {
                            Text(
                                "Profile",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        },

                        colors = TopAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = MaterialTheme.colorScheme.onSurface,
                            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                        )

                    )
                }
            },
        ) {

            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(8.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val initials =
                    userData?.username?.split(" ")?.joinToString("")
                    { it.take(1) }?.uppercase() ?: "NA"

                Box(
                    modifier = Modifier
                        .size(80.dp) // Size of the circular icon
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ) // Circular shape with background color
                        .border(2.dp, Color.White, shape = CircleShape) // Optional border
                        .padding(8.dp), // Padding inside the circle
                    contentAlignment = Alignment.Center
                ) {
                    if (initials != null) {
                        Text(
                            text = initials,
                            color = Color.White,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(gradient)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentHeight()
                    ) {

                        ProfileDetails(userData)


                        TextButton(
                            onClick = {
                                logout()
                            },
                        ) {
                            androidx.compose.material.Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = "Sign Out",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Spacer(modifier = Modifier.padding(vertical = 4.dp))
                        androidx.compose.material.Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = "1Click Tech ERP v1.0.0"
                        )
                        Spacer(modifier = Modifier.padding(vertical = 7.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun ProfileDetails(userData: UserData?) {

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var mobileNum by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var birthday by remember {
        mutableStateOf("")
    }
    var isProfileEditingEnabled by remember { mutableStateOf(false) }
    var context = LocalContext.current
    Column() {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            ProfileTextField(
                label = "First Name",
                text =
                userData?.name?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } + " " + userData?.surname?.capitalize(
                    Locale.ROOT
                ),
                onTextChange = { fullName = it },
                isEnabled = isProfileEditingEnabled
            )


//            userName.let {
            ProfileTextField(
                label = "Username",
                text =
                userData?.username?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
                    ?: "NA"
//                    it
                ,
                onTextChange = { username = it },
                isEnabled = false
            )
//            }

            ProfileTextField(
                label = "Email",
                text = userData?.email?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
                    ?: "NA",
                onTextChange = { email = it },
                isEnabled = isProfileEditingEnabled,
            )


            ProfileTextField(
                label = "Mobile",
                text = userData?.mobileNumber?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
                    ?: "NA",
                onTextChange = { mobileNum = it },
                isEnabled = isProfileEditingEnabled,
            )


            ProfileTextField(
                label = "Gender",
                text = userData?.gender?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
                    ?: "NA",
                onTextChange = { gender = it },
                isEnabled = isProfileEditingEnabled
            )


            birthday?.let {
                ProfileTextField(
                    label = "Birth date",
                    text =
                    userData?.birthDate?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    }
                        ?: "NA",
                    onTextChange = { birthday = it },
                    isEnabled = isProfileEditingEnabled,
                )
            }

//            if (isDatePickerVisible) {
//                DatePicker(
//                    selectedDate = selectedDate,
//                    onDateChange = { newDate ->
//                        selectedDate = newDate
//                        birthday = formatDate(newDate)
//                    }
//                )
//            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTextField(
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
    isEnabled: Boolean,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(vertical = 6.dp)

    ) {
        Text(
            label,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .width(110.dp)
                .padding(bottom = 10.dp)
        )
        Column {
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                enabled = isEnabled,
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W300,
                    fontFamily = FontFamily(Font(R.font.gilroy_semibold))
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp, Color(0xFF544C4C),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(16.dp)
                            .background(Color.Transparent)
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = "Placeholder", // Replace with your placeholder text if needed
                                style = TextStyle(
                                    color = Color(0xFF544C4C),
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.gilroy_semibold))
                                )
                            )
                        }
                        innerTextField()
                    }

                },
                modifier = Modifier.fillMaxWidth()
            )
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 2.dp)
//                    .height(1.dp)
//                    .background(Color.LightGray)
//            )
        }
    }
}