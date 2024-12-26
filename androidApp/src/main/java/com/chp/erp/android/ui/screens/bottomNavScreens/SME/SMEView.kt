package com.chp.erp.android.ui.screens.bottomNavScreens.SME

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.chp.erp.android.apiServices.ApiViewModel
import com.chp.erp.android.ui.screens.bottomNavScreens.createSelectionState
import com.chp.lms.Services.Dataclass.InsuranceTypeUsingSegmentIDData
import com.chp.lms.Services.Dataclass.InsurerGroupData
import com.chp.lms.Services.Dataclass.PolicySegmentResponse
import com.chp.lms.Services.Dataclass.ProductData
import com.chp.lms.Services.Dataclass.RenewalTypes
import com.chp.lms.Services.Dataclass.SlabData



@SuppressLint("UnrememberedMutableState")
@Composable
fun SMEView(context: Context, viewModel: ApiViewModel, mainNavController: NavHostController) {
    var slabTypes by mutableStateOf<SlabData?>(null)
    var productTypes by mutableStateOf<ProductData?>(null)
    var insurerGroups by mutableStateOf<InsurerGroupData?>(null)
    var insuranceTypesById by mutableStateOf<InsuranceTypeUsingSegmentIDData?>(null)
    var insurerTypes by mutableStateOf<SlabData?>(null)
    var policySegments by mutableStateOf<PolicySegmentResponse?>(null)
    var renewalTypes by mutableStateOf<RenewalTypes?>(null)


    val slabData by viewModel.getAllSlabTypes.collectAsState()
    val productData by viewModel.getProductTypes.collectAsState()
    val insurerGroupData by viewModel.getInsurerGroups.collectAsState()
    val insuranceTypesByIdData by viewModel.getInsuranceTypeByID.collectAsState()
    val allInsurerTypes by viewModel.getAllInsurerTypes.collectAsState()
    val policySegmentsData by viewModel.getPolicySegments.collectAsState() // This is an direct list

    // Data for various fields
    val allSlabData = slabData?.data
    val allproductData = productData?.data
    val allInsurerGroupData = insurerGroupData?.data
    val allInsuranceTypesByIdData  = insuranceTypesByIdData?.data
    val allInsurerTypesData = allInsurerTypes?.data

    val (slabTypesState, slabTypesDropdownState) = createSelectionState()
    val (productTypesState, productTypesDropdownState) = createSelectionState()
    val (insurerGroupsState, insurerGroupsDropdownState) = createSelectionState()
    val (insuranceTypesByIdState, insuranceTypesByIdDropdownState) = createSelectionState()
    val (insurerTypesState, insurerTypesDropdownState) = createSelectionState()
    val (renewalTypesState, renewalTypesDropdownState) = createSelectionState()

}