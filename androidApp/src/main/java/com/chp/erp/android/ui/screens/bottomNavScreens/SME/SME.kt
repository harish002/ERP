package com.chp.erp.android.ui.screens.bottomNavScreens.SME

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chp.erp.android.R
import com.chp.erp.android.apiServices.ApiViewModel
import com.chp.erp.android.ui.bottombarGraph.BottomBarScreen
import com.chp.erp.android.ui.component.CustBtn
import com.chp.erp.android.ui.screens.SelectionView
import com.chp.erp.android.ui.screens.bottomNavScreens.createSelectionState
import com.chp.lms.Services.Dataclass.GeneralPolicyRatePayload
import com.chp.lms.Services.Dataclass.InsuranceTypeUsingSegmentIDData
import com.chp.lms.Services.Dataclass.InsurerGroupData
import com.chp.lms.Services.Dataclass.PolicySegmentResponse
import com.chp.lms.Services.Dataclass.ProductData
import com.chp.lms.Services.Dataclass.RenewalTypes
import com.chp.lms.Services.Dataclass.SearchPolicyRatePayload
import com.chp.lms.Services.Dataclass.SlabData
import com.chp.lms.android.Services.Methods
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SMEView(context: Context, viewModel: ApiViewModel, mainNavController: NavHostController) {
    val slabData by viewModel.getAllSlabTypes.collectAsState()
    val productData by viewModel.getProductTypes.collectAsState()
    val insurerGroupData by viewModel.getInsurerGroupsObj1.collectAsState()
    val insuranceTypesByIdData by viewModel.getInsuranceTypeByID.collectAsState()
    val allInsurerTypes by viewModel.getAllInsurerTypes.collectAsState()
    val allRenewalTypes by viewModel.getAllRenewalTypes.collectAsState()
    val policySegmentsData by viewModel.getPolicySegments.collectAsState() // This is an direct list

    // Data for various fields
    val allSlabData = slabData?.data
    val allproductData = productData?.data
    val allInsurerGroupData = insurerGroupData?.data
    val allInsuranceTypesByIdData = insuranceTypesByIdData?.data
    val allInsurerTypesData = allInsurerTypes?.data
    val allrenewalTypes = allRenewalTypes?.data

    val (slabTypesState, slabTypesDropdownState) = createSelectionState()
    val (productTypesState, productTypesDropdownState) = createSelectionState()
    val (insurerGroupsState, insurerGroupsDropdownState) = createSelectionState()
    val (insuranceTypesByIdState, insuranceTypesByIdDropdownState) = createSelectionState()
    val (insurerTypesState, insurerTypesDropdownState) = createSelectionState()
    val (renewalTypesState, renewalTypesDropdownState) = createSelectionState()

    val coroutineScope = rememberCoroutineScope()

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
                    BottomBarScreen.SME_Data
                        .route?.let { mainNavController.navigate(it) }
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
                        Methods()
                            .retrieve_Token(context)
                            ?.let { it1 ->
                                viewModel.filterGeneralSearchData(
                                    it1, filterpayload
                                )
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
                            val allInsurerTypesDeferred =
                                async { viewModel.getAllInsurerTypes(token) }
                            allInsurerTypesDeferred.await()

                        }
                        // Use async to call multiple suspend functions concurrently
                        val slabDataDeferred = async {
                            Methods().retrieve_userID(context)?.let {
                                viewModel.getAllSlabTypes(
                                    it
                                )
                            }
                        }
                        val productDeferred = async { Methods().retrieve_userID(context)
                            ?.let { viewModel.getProductTypes(it) } }
                        val insuranceGroupDeferred = async { Methods().retrieve_userID(context)
                            ?.let { viewModel.getInsurerGroups(it) } }
                        val insuranceTypesBySegmentIdDeffered =
                            async { Methods().retrieve_userID(context)?.let {
                                viewModel.getInsuranceTypeByPolicySegments("3e2d3437-7949-4047-bb25-97392928423a",
                                    it
                                )
                            } }


                        // Await all results
//                        userDeferred.await()
                        slabDataDeferred.await()
                        productDeferred.await()
                        insuranceGroupDeferred.await()
                        insuranceTypesBySegmentIdDeffered.await()


                    } catch (e: Exception) {
                        println("Error occurred: ${e.message}")
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

                        // Slab SelectionView
                        SelectionView(
                            selectionTitle = "Slab",
                            staticValue = "Please select a Slab",
                            selectedValue = slabTypesState,
                            dropDownViewSelected = slabTypesDropdownState,
                            listTypes = allSlabData
                        )

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



