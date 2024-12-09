package com.chp.erp.android.ui.screens.bottomNavScreens.Motor

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chp.erp.android.R
import com.chp.erp.android.apiServices.ApiViewModel
import com.chp.erp.android.ui.screens.PolicyListView

@Composable
fun VehicleData(viewModel: ApiViewModel, mainNavController: NavController) {

    val vechiclepolicyRatesList by viewModel.getfilterPolicyRateData.collectAsState()

    LazyColumn {
        if (vechiclepolicyRatesList.isEmpty()) {
            item {
                // Display a message when there are no items
                Image(
                    painter = painterResource(R.drawable.not_found),
                    contentDescription = "Not Found Image"
                )
                Text(
                    text = "No Data Available",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Add some padding for better appearance
                    style = MaterialTheme.typography.labelMedium, // Use appropriate text style
                    color = MaterialTheme.colorScheme.onSurface // Adjust color as needed
                )
            }
        } else {
//            If there are items, display them in the list
            itemsIndexed(vechiclepolicyRatesList) { index, item ->
                if (item != null) {
//                    REOPEN
                    PolicyListView(item, index + 1, mainNavController = mainNavController)
                }
            }
        }
    }

}
