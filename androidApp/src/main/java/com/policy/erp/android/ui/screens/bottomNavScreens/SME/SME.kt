package com.policy.erp.android.ui.screens.bottomNavScreens.SME

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.policy.erp.android.R
import com.policy.erp.android.apiServices.ApiViewModel
import com.policy.erp.android.ui.bottombarGraph.BottomBarScreen
import com.policy.erp.android.ui.component.CustBtn
import com.policy.erp.android.ui.screens.SelectionView
import com.policy.erp.android.ui.screens.bottomNavScreens.createSelectionState
import com.policy.lms.Services.Dataclass.GeneralPolicyRatePayload
import com.policy.lms.Services.Dataclass.InsurerX
import com.policy.lms.Services.Dataclass.PolicyRateData
import com.policy.lms.android.Services.Methods
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SMEView(context: Context, viewModel: ApiViewModel, mainNavController: NavHostController) {

    val productData by viewModel.getProductTypes.collectAsState()
    val insurerGroupData by viewModel.getInsurerGroupsObj1.collectAsState()
    val insuranceTypesByIdData by viewModel.getInsuranceTypeByID.collectAsState()
    val allInsurerTypes by viewModel.getInsurerByInsurerGrp.collectAsState()
    val allRenewalTypes by viewModel.getAllRenewalTypesBySegmentId.collectAsState()
    val policySegmentsData by viewModel.getPolicySegments.collectAsState() // This is an direct list

    // Data for various fields
    val allproductData = productData?.data
    val allInsurerGroupData = insurerGroupData?.data
    val allInsuranceTypesByIdData = insuranceTypesByIdData?.data
    val allrenewalTypes = allRenewalTypes?.data
    var allInsurerTypesData = allInsurerTypes?.data


    val (slabTypesState, slabTypesDropdownState) = createSelectionState()
    val (productTypesState, productTypesDropdownState) = createSelectionState()
    val (insurerGroupsState, insurerGroupsDropdownState) = createSelectionState()
    val (insuranceTypesByIdState, insuranceTypesByIdDropdownState) = createSelectionState()

    val (insurerTypesState, insurerTypesDropdownState) = createSelectionState()

    val (renewalTypesState, renewalTypesDropdownState) = createSelectionState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(insurerGroupsState.value["name"]) {
        if(insurerGroupsState.value["id"].isNullOrBlank()){
        }
        else{
            Methods().retrieve_Token(context)?.let {
                insurerGroupsState.value["id"]?.let { it1 ->
                    viewModel.fetchInsurerByInsurerGrp(
                        it,
                        id = it1,
                    )
                }
            }
            allInsurerTypesData = allInsurerTypes?.data
        }

    }


    Scaffold(
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
                                painter = painterResource(R.drawable.sme),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "SME "
                            )
                            Spacer(Modifier.padding(2.dp))
                            Text(
                                "SME",
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
        bottomBar = {
            Box(modifier = Modifier.padding(8.dp)) {
                CustBtn(text = "Submit", isblue = true,) {
                    val filterpayload = GeneralPolicyRatePayload(
                        insurer_id = insurerTypesState.value["id"] ?: "",
                        renewal_type_id = renewalTypesState.value["id"] ?: "",
                        insurance_type_id = insuranceTypesByIdState.value["id"] ?: "",
                        policy_segment_id = "3e2d3437-7949-4047-bb25-97392928423a",
                        slab_id = slabTypesState.value["id"] ?: "",
                        product_id = productTypesState.value["id"] ?: "",
                        ppt_id = "",
                        insurer_group_id = insurerGroupsState.value["id"] ?: "",
                        payouts = "",
                        payins = "",
                        remarks = "",
                        description = "",
                    )
                    coroutineScope.launch {
                        val result = Methods()
                            .retrieve_Token(context)
                            ?.let { it1 ->
                                viewModel.filterGeneralSearchData(
                                    it1, filterpayload
                                )
                            }

                        if (result.isNullOrEmpty()) {
                            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "match found", Toast.LENGTH_SHORT).show()
                            BottomBarScreen.SME_Data.route?.let { mainNavController.navigate(it) }
                        }
                    }
                }
            }
        },
        content = { paddingValues ->
            LazyColumn(
                Modifier
                    .padding(paddingValues)
                    .padding(8.dp)
            )
            {

                coroutineScope.launch {
                    try {
                        Methods().retrieve_Token(context)?.let { token ->
                            // Use async to call multiple suspend functions concurrently
                            val productDeferred = async { viewModel.getProductTypes(token) }
                            val renewalTypeDeferred = async { viewModel.fetchAllRenewalTypesBySegmentId(token,
                                "3e2d3437-7949-4047-bb25-97392928423a") }
                            val insuranceGroupDeferred = async { viewModel.getInsurerGroups(token) }
                            val insuranceTypesBySegmentIdDeferred = async {
                                viewModel.getInsuranceTypeByPolicySegments(
                                    "3e2d3437-7949-4047-bb25-97392928423a",
                                    token
                                )
                            }
                            val allInsurerTypesDeferred = async { viewModel.getAllInsurerTypes(token) }

                            // Await all results
                            awaitAll(
                                productDeferred,
                                renewalTypeDeferred,
                                insuranceGroupDeferred,
                                insuranceTypesBySegmentIdDeferred,
                                allInsurerTypesDeferred
                            )
                        }
                    } catch (e: Exception) {
                        println("Error occurred: ${e.message}")
                        // Optionally handle loading state on error
                    }
                }
                item {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
// Vehicle Type SelectionView
//                item {
                        SelectionView(
                            selectionTitle = "Insurance Type",
                            staticValue = "Please select a Insurance Type",
                            selectedValue = insuranceTypesByIdState,
                            dropDownViewSelected = insuranceTypesByIdDropdownState,
                            listTypes = allInsuranceTypesByIdData
                        )

                        // Renewal Type SelectionView
                        SelectionView(
                            selectionTitle = "Renewal Type",
                            staticValue = "select a renewal type",
                            selectedValue = renewalTypesState,
                            dropDownViewSelected = renewalTypesDropdownState,
                            listTypes = allrenewalTypes
                        )

//                        // Slab SelectionView
//                        SelectionView(
//                            selectionTitle = "Slab",
//                            staticValue = "Please select a Slab",
//                            selectedValue = slabTypesState,
//                            dropDownViewSelected = slabTypesDropdownState,
//                            listTypes = allSlabData
//                        )

                        // Insurer Group SelectionView
                        SelectionView(
                            selectionTitle = "Insurer Group",
                            staticValue = "select an Insurer Group",
                            selectedValue = insurerGroupsState,
                            dropDownViewSelected = insurerGroupsDropdownState,
                            listTypes = allInsurerGroupData
                        )

                        // Insurer SelectionView
                        SelectionView(
                            selectionTitle = "Insurer",
                            staticValue = "Please select a Insurer",
                            selectedValue = insurerTypesState,
                            dropDownViewSelected = insurerTypesDropdownState,
                            listTypes = allInsurerTypesData
                        )
                        // product SelectionView
                        SelectionView(
                            selectionTitle = "product",
                            staticValue = "select a city category",
                            selectedValue = productTypesState,
                            dropDownViewSelected = productTypesDropdownState,
                            listTypes = allproductData
                        )


                    }
                }


            }
        }
    )

}



