package com.policy.erp.android.ui.screens.bottomNavScreens.Setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.policy.erp.android.ERPTheme
import com.policy.erp.android.R
import com.policy.erp.android.ui.bottombarGraph.BottomBarScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Setting(
    mainNavController: NavController,
    context: Context,

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

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.scrim)
                ) {
                    TopAppBar(
                        title = {
                            Text(
                                "Setting",
                                color = MaterialTheme.colorScheme.onSurface,
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
                Modifier
                    .padding(it)
                    .padding(16.dp, vertical = 8.dp)
            ) {
                item{
                    Text(
                        text = "Account Setting",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(gradient)

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
//                        .clickable { showPopup = !showPopup }
                                .padding(6.dp),
                        ) {

                            SettingComp(R.drawable.profile,"Edit Profile"){
                                BottomBarScreen.Profile.route?.let { it1 ->
                                    mainNavController.navigate(
                                        it1
                                    )
                                }
                            }

                            SettingComp(R.drawable.security,"Security"){
                                BottomBarScreen.Security.route?.let { it1 ->
                                    mainNavController.navigate(
                                        it1
                                    )
                                }
                            }

                            SettingComp(R.drawable.vehicle_history,"Vehicle History"){
                                Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
                            }

                            SettingComp(R.drawable.update,"Update"){
                                Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                item{
                    Text(
                        text = "Support and About",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(gradient)

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
//                        .clickable { showPopup = !showPopup }
                                .padding(6.dp),
                        ) {
                            SettingComp(R.drawable.privacy_policy,"Privacy Policy"){
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://1clickpolicy.com/privacy-policy")
                                )
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Failed to open link",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            }
                            SettingComp(R.drawable.terms_policies,"Terms and Policies"){
                                // Create an Intent to open the URL
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://1clickpolicy.com/terms-and-conditions")
                                )
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Failed to open link",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            }
                        }
                    }
                }

                item{
                    Text(
                        text = "Network",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(gradient)

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
//                        .clickable { showPopup = !showPopup }
                                .padding(6.dp),
                        ) {
                            SettingComp(R.drawable.motor,"Motor Cashless"){
                                BottomBarScreen.MotorCashless.route?.let { it1 ->
                                    mainNavController.navigate(
                                        it1
                                    )
                                }
                            }
                            SettingComp(R.drawable.health,"Health Cashless"){
                                BottomBarScreen.MotorCashless.route?.let { it1 ->
                                    mainNavController.navigate(
                                        it1
                                    )
                                }
                            }
                        }
                    }
                }

                item{
                    Text(
                        text = "Action",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(gradient)

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
//                        .clickable { showPopup = !showPopup }
                                .padding(6.dp),
                        ) {
                            SettingComp(R.drawable.report_problem,"Report a Problem"){
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://1clickpolicy.com/complaint")
                                )
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Failed to open link",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            }
                            SettingComp(R.drawable.log_out,"Log Out"){
                                logout()
                            }
                        }
                    }
                }


            }
        }
    }
}


@Composable
fun SettingComp(drawable: Int,title:String,onclick : ()-> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onclick() }
            .padding(16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {

        Icon(
            painter = painterResource(drawable),
            contentDescription = "profile",
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.padding(horizontal = 6.dp))
        Text(
            text =
            title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .weight(0.2f)
        )
    }
}