package com.chp.erp.android.ui.screens.bottomNavScreens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chp.erp.android.ERPTheme
import com.chp.erp.android.R
import com.chp.erp.android.apiServices.ApiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notification(
    mainNavController: NavHostController,
    viewModel: ApiViewModel,
    context: Context
) {
    val scrollState = rememberScrollState()
    var loading by remember { mutableStateOf(true) }
    var visible by remember { mutableStateOf(false) }
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFEBF1FF),
        ), // Customize your colors here
        start = Offset(100f, 0f),
        end = Offset(700f, 0f) // Adjust the end point for gradient direction
    )


    ERPTheme {


        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            Icon(
                                Icons.Outlined.ArrowBack,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "Back Btn Icon",
                                modifier = Modifier
                                    .padding(6.dp)
                                    .clickable { mainNavController.navigateUp() }
                            )
                        },
                        title = {
                            Text(
                                "Notification",
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
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                items(4) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(gradient)


                    ) {
                        NotificationComp(
                            R.drawable.notification, "Notificaiton Title",
                            des = "Notification Description blah balh"
                        ) {

                        }

                    }
                    Spacer(Modifier.padding(6.dp))
                }
            }
        }
    }
}


@Composable
fun NotificationComp(drawable: Int, title: String, des: String, onclick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onclick() }
            .padding(4.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {

        Icon(
            painter = painterResource(drawable),
            contentDescription = "profile",
            Modifier.size(26.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.padding( 6.dp))

        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                modifier = Modifier
            )
            Spacer(Modifier.padding(vertical = 2.dp))
            Text(
                text = des,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier

            )
        }
    }
}