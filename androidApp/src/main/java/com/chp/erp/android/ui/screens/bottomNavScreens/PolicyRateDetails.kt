package com.chp.erp.android.ui.screens.bottomNavScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chp.erp.android.ERPTheme
import com.chp.erp.android.apiServices.ApiViewModel
import com.chp.erp.android.ui.screens.GridItem
import com.chp.lms.Services.Dataclass.PolicyRateData
import com.google.gson.Gson

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Poicy_Rate_Details(
    mainNavController: NavController,
    data: String,
    viewModel: ApiViewModel
) {


    val policyRatesList by viewModel.getPolicyRates.collectAsState()



    ERPTheme {
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
                                "Policy Rates",
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
        ) { it ->

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(12.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
//                    var specificPolicyRate by mutableStateOf<List<PolicyRateData?>>(emptyList())
//
//                    LaunchedEffect(true) {
//
//                        specificPolicyRate =specificPolicyRate1
//
//                        Log.d("lagValue",specificPolicyRate1.toString())
//                    }
                    val specificPolicyRate1 = policyRatesList.filter { i -> i?.id == data }
                    specificPolicyRate1.firstOrNull()?.let { it2 ->

                        GridItem("Payout %", it2.payouts)

                        GridItem("Insurer", it2.insurer.name)

                        GridItem("Insurance Type", it2.insurance_type.name)

                        GridItem("Vehicle Type", it2.vehicle_model.vehicle_type.name)

                        GridItem("Renewal Type", it2.renewal_type.name)

                        GridItem("Fuel Type", it2.fuel_type.name)

                        GridItem("State", it2.city.state.name)

                        it2.city.city_category?.let { GridItem("City Category", it.name) }

                        GridItem("City", it2.city.name)

                        GridItem("NCB", it2.status.toString())

                    }

                }
            }
        }
    }

}

@Composable
fun PolicyRateComp(data: PolicyRateData) {
   val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer,
        ), // Customize your colors here
        start = Offset(100f, 0f),
        end = Offset(700f, 0f) // Adjust the end point for gradient direction
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(12.dp)),
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {

                Column(
                    modifier = Modifier
                        .weight(0.7f),
                    verticalArrangement = Arrangement.Center,
                ) {

                    GridItem("Payout %", data.payouts)
                }


            }
        }

    }
}