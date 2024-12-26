package com.chp.erp.android.ui.screens.bottomNavScreens.Setting.Network

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chp.erp.android.R
import com.chp.erp.android.apiServices.ApiViewModel
import com.chp.erp.android.ui.bottombarGraph.BottomBarScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthCashless(mainNavController: NavController,context: Context) {


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

                        Row {
                            Icon(
                                painter = painterResource(R.drawable.healthlink_icon),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "motor Icon"
                            )
                            Spacer(Modifier.padding(2.dp))
                            Text(
                                "Health Cashless",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
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
        LazyColumn(Modifier.padding(it)) {
            item {
                networkComp("ICICI Lombard","https://www.icicilombard.com/cashless-hospitals",context)
                networkComp("Bajaj Allianz","https://www.bajajallianz.com/general-insurance-features/health-insurance/cashless-health-insurance.html",context)
                networkComp("Manipal Cigna","https://www.manipalcigna.com/prime-cashless-opd-network",context)
                networkComp("Niva Bupa","https://www.nivabupa.com/health-insurance-plans/get-quote.html?gclid=CjwKCAiA6t-6BhA3EiwAltRFGOksb6FwOeZ7rCGJrND06modJSnf_1U8Z8Dwb0wWLBVB0AqTcyVpmhoCdQsQAvD_BwE&cid=S_Brand_E_NB_Health_Insurance&utm_source=google&utm_medium=cpc&utm_campaign=Google_Search_Niva_Brand_Exact_New&utm_term=niva%20bupa%20health&utm_content=Niva_Bupa_Health_Insurance&ef_id=CjwKCAiA6t-6BhA3EiwAltRFGOksb6FwOeZ7rCGJrND06modJSnf_1U8Z8Dwb0wWLBVB0AqTcyVpmhoCdQsQAvD_BwE:G:s&s_kwcid=AL!7961!3!638197148300!e!!g!!niva%20bupa%20health",context)
                networkComp("Hdfc Ergo","https://www.hdfcergo.com/campaigns/hdfc-ergo-health-insurance-2?&utm_source=google_search_1&utm_medium=cpc&utm_campaign=Health_Search_Brand_Neev-Phrase&utm_adgroup=Generic-Insurance&adid=632972567247&utm_term=hdfc%20ergo%20health%20insurance&utm_network=g&utm_matchtype=p&utm_device=c&utm_location=1007765&utm_sitelink={sitelink}&utm_placement=&ci=googlesearch&SEM=1&gad_source=1&gclid=CjwKCAiA6t-6BhA3EiwAltRFGOjad3aqCEwAYDEQX67CTksTdt0BoiPgg7n3zY6GGHq9t40WVn0D_xoCeSgQAvD_BwE",context)
                networkComp("Hdfc Ergo Cashless Garages","https://www.hdfcergo.com/locators/cashless-garages-networks",context)
                networkComp("Care Insurance","https://www.careinsurance.com/health-plan-network-hospitals.html\n",context)
                networkComp("Star Health","https://www.starhealth.in/lookup/hospital/",context)
            }
        }
    }

}

@Composable
fun networkComp(title: String, url: String,context:Context) {
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
            .clickable {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("$url")
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
                Text(
                    text =
                    title,
                    color =
                    Color(0xFF6C757D)
//                            Materia lTheme.colorScheme.onSurface
                    ,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 2.dp)
                )


                Spacer(
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(4.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.arrow_forward_ios),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Back Btn Icon",
                )

            }
        }

    }
}