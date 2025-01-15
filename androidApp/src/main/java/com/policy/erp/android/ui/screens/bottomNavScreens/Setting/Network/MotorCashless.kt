package com.policy.erp.android.ui.screens.bottomNavScreens.Setting.Network

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.policy.erp.android.R


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MotorCashless(mainNavController: NavController, context: Context) {

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
                                painter = painterResource(R.drawable.motor),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "motor Icon"
                            )
                            Spacer(Modifier.padding(2.dp))
                            Text(
                                "Motor Cashless",
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
                networkComp("ICICI Lombard","https://www.icicilombard.com/cashless-garages",context)
                networkComp("Bajaj Allianz","https://www.bajajallianz.com/general-insurance-features/car-insurance/cashless-claim-settlement.html",context)
                networkComp("TATA AIG","https://www.tataaig.com/buy-online/motor-insurance/car-insurance?utm_source=google&utm_medium=cpc&utm_campaign={4W_TAGIC_Brand_Category_Car_Desktop_EM}-tata%20aig%20motor-52147524-87714574775-kwd-21249036639&utm_content=553121219655&gad_source=1&gclid=CjwKCAiA6t-6BhA3EiwAltRFGKLmVD8IUpWTpjRYaDkJFqxq7z-ZN8Vacr7_5DhgHh_U33kQBpUycRoCKmoQAvD_BwE",context)
                networkComp("Magma HDI","https://www.magmahdi.com/de/more/contact-us?f=g",context)
                networkComp("Royal Sundaram","https://www.royalsundaram.in/cashless-garage",context)
            }
        }
    }

}
