package com.example.erp.android.UI.Screens.BottomNavScreens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.erp.android.ERPTheme
import com.example.erp.android.UI.Graphs.Graph
import com.example.lms.android.Services.ApiViewModel
import com.example.lms.android.Services.Methods

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

    ERPTheme {
        val userData by viewModel.getUserdata.collectAsState()
        LaunchedEffect(context) {
            Methods().retrieve_Token(context)?.let {
                viewModel.getAllPolicyRates(it)
                viewModel.getUserWhoLoggedIn(it)
                loading = false
            }
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Text(
                        "Account",
                        modifier = Modifier.padding(start = 10.dp)
                    )
                })
            },
        ) {

            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onBackground)
                    .verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier
                        .padding(26.dp)
                        .drawBehind {
                            drawCircle(
                                color = Color.Blue.copy(alpha = 0.5f),
                                radius = this.size.maxDimension
                            )
                        },
                    text = "Hello",
                )


                userData?.userData?.name?.let { it1 ->
                    Text(
                        modifier = Modifier.padding(vertical = 10.dp),
                        text = it1,
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Mail,
                        tint = Color.Blue,
                        contentDescription = "Mail Icon"
                    )
                    userData?.email?.let { it1 ->
                        Text(
                            text = it1,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge

                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentHeight()
                ) {
                    Text(
                        text = "Support",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp, bottom = 14.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "About LMS",
                            color = Color.Black,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .wrapContentWidth()
                                .clickable { }
                                .padding(vertical = 18.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowRight,
                            tint = Color.Black,
                            contentDescription = "Right Arrow"
                        )
                    }

                    Text(
                        text = "Help and Support",
                        color = Color.Black,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .wrapContentWidth()
                            .clickable { }
                            .padding(vertical = 18.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Share the LMS app",
                        color = Color.Black,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .wrapContentWidth()
                            .clickable { }
                            .padding(vertical = 18.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )

                }
                Spacer(modifier = Modifier.padding(vertical = 7.dp))

                TextButton(
                    onClick = {
                        logout()
                    },
                ) {
                    androidx.compose.material.Text(
                        text = "Sign Out",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                androidx.compose.material.Text(
                    text = "1Click Tech LMS v1.0.0"
                )
                Spacer(modifier = Modifier.padding(vertical = 7.dp))
            }
        }
    }
}