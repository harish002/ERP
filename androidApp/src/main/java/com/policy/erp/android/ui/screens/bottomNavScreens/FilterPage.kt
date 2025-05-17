package com.policy.erp.android.ui.screens.bottomNavScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.policy.erp.android.ERPTheme
import com.policy.erp.android.R
import com.policy.erp.android.apiServices.ApiViewModel
import com.policy.erp.android.apiServices.project_id
import com.policy.erp.android.ui.component.CustBtn
import com.policy.erp.android.ui.screens.PolicyListView
import com.policy.erp.android.ui.screens.SelectionView
import com.policy.erp.android.ui.screens.bottomNavScreens.Motor.VehicleBrandSelection
import com.policy.erp.android.ui.screens.bottomNavScreens.Motor.VehicleCitySelection
import com.policy.erp.android.ui.screens.bottomNavScreens.Motor.VehicleTypeSelection
import com.policy.erp.android.ui.screens.bottomNavScreens.Motor.getItemBrandName
import com.policy.erp.android.ui.screens.bottomNavScreens.Motor.getItemCityName
import com.policy.erp.android.ui.screens.bottomNavScreens.Motor.getItemName
import com.policy.lms.Services.Dataclass.SearchPolicyRatePayload
import com.policy.lms.android.Services.Methods
import com.policy.lms.android.Services.getfirstInstall
import com.policy.lms.android.Services.saveFirstInstall
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    mainNavController: NavController,
    viewModel: ApiViewModel,
    logout: () -> Unit,
) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(true) }
    var dataloading by remember { mutableStateOf(true) }

    val policyRatesList by viewModel.getPolicyRates.collectAsState()


    val vehiclType by viewModel.getVehicleTypes.collectAsState()
    val vehiclBrand by viewModel.getVehicleBrands.collectAsState()
    val vehicleModel by viewModel.getVehicleModels.collectAsState()
    val fuelType by viewModel.getFuelTypes.collectAsState()
    //location Details
    val allState by viewModel.getAllStates.collectAsState()
    val cityCategory by viewModel.getAllCityCategory.collectAsState()
    val allCities by viewModel.getAllCities.collectAsState()
    //policy Details
    val allInsuranceTypes by viewModel.getAllInsuranceTypes.collectAsState()
    val allRenewalTypes by viewModel.getAllRenewalTypes.collectAsState()
    val allInsurerTypes by viewModel.getAllInsurerTypes.collectAsState()

    val sheetState =
        androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var filterSheet by remember { mutableStateOf(false) }

    // Initialize states for vehicle type and fuel type
    val (vehicleTypeState, vehicleTypeDropdownState) = createSelectionState()
    val (fuelTypeState, fuelTypeDropdownState) = createSelectionState()
    val (vehicleBrandState, vehicleBrandDropdownState) = createSelectionState()
    val (vehicleModelState, vehicleModelDropdownState) = createSelectionState()

    val (ncbState, ncbDropdownState) = createSelectionState()

    // Location Details
    val (stateState, stateDropdownState) = createSelectionState()
    val (cityCategoryState, cityCategoryDropdownState) = createSelectionState()
    val (cityState, cityDropdownState) = createSelectionState()

    // Policy Details
    val (insuranceTypeState, insuranceTypeDropdownState) = createSelectionState()
    val (renewalTypeState, renewalTypeDropdownState) = createSelectionState()
    val (insurerState, insurerDropdownState) = createSelectionState()

    fun resetAllStates(): SearchPolicyRatePayload {
        vehicleTypeState.value = mapOf("id" to "", "name" to "")
        fuelTypeState.value = mapOf("id" to "", "name" to "")
        ncbState.value = mapOf("name" to "")

        stateState.value = mapOf("id" to "", "name" to "")
        cityCategoryState.value = mapOf("id" to "", "name" to "")
        cityState.value = mapOf("id" to "", "name" to "")

        insuranceTypeState.value = mapOf("id" to "", "name" to "")
        renewalTypeState.value = mapOf("id" to "", "name" to "")
        insurerState.value = mapOf("id" to "", "name" to "")

        return SearchPolicyRatePayload(
            state_id = stateState.value["id"] ?: "",
            city_id = cityState.value["id"] ?: "",
            city_category_id = cityCategoryState.value["id"] ?: "",
            vehicle_type_id = vehicleTypeState.value["id"] ?: "",
            vehicle_model_id = "",
            renewal_type_id = renewalTypeState.value["id"] ?: "",
            insurance_type_id = insuranceTypeState.value["id"] ?: "",
            insurer_id = insurerState.value["id"] ?: "",
            fuel_type_id = fuelTypeState.value["id"] ?: "",
            status = if (ncbState.value["name"].isNullOrEmpty()) {
                ""
            } else {
                if (ncbState.value["name"] == "YES") "1" else "0"
            },
            page = 1,
            size = 20
        )
    }
    // Data for various fields
    val vehicleTypes = vehiclType?.data
    val fuelTypes = fuelType?.data
    val vehicleBrandTypes = vehiclBrand?.data
    val vehicleModelTypes = vehicleModel?.data

    val ncbTypes = listOf("YES", "NO")  // Static for NCB Type
    val states = allState?.data
    val cityCategories = cityCategory?.data
    val cities = allCities?.data
    val insuranceTypes = allInsuranceTypes?.data
    val renewalTypes = allRenewalTypes?.data
    val insurers = allInsurerTypes?.data


    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    var searchVal by remember { mutableStateOf("") }
    val colors: TextFieldColors = TextFieldDefaults.colors(
        cursorColor = Color.Black,
        disabledLabelColor = Color(0xFF949494),
        focusedLabelColor = Color(0xFF4E4E4E),
        unfocusedTextColor = Color(0xFF949494),
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = MaterialTheme.colorScheme.background,
        unfocusedIndicatorColor = Color(0xFFD0D0D0)
    )



    ERPTheme {
        val coroutineScope = rememberCoroutineScope()
        val payload = SearchPolicyRatePayload(
            state_id = "",
            city_id = "",
            city_category_id = "",
            vehicle_type_id = "",
            vehicle_model_id = "",
            renewal_type_id = "",
            insurance_type_id = "",
            insurer_id = "",
            fuel_type_id = "",
            status = "", page = 1, size = 50
        )


        LaunchedEffect(Unit) {
            // Launching a coroutine in LaunchedEffect to initialize data

//            coroutineScope.launch {


            Methods().retrieve_Token(context)?.let { token ->
                if (getfirstInstall(context) == true) {
                    saveFirstInstall(context)
                    Methods().retrieve_DToken(context)?.let { Dtoken->
                        Methods().retrieve_userID(context)?.let { Uid->
                            viewModel.registerDeviceForNotification(
                                token = token,
                                projectId = project_id,
                                userId = Uid,
                                deviceToken = Dtoken
                            )
                        }
                    }
                }
                //Get Policy Rate Data
                viewModel.getAllPolicyRates(
                    token = token,
                    context = context,
                    payload = payload,
                    logout = logout
                )
            }

            delay(2000)


            dataloading = false
        }


//        val allCategorylist = viewModel.allCourseCate.collectAsState()
//        val allPublishlist = viewModel.allPublishesCourses.collectAsState()
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
//                .nestedScroll(scrollBehavior.nestedScrollConnection)
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.scrim)
                ) {
                    TopAppBar(
                        modifier = Modifier
                            .background(Color.Transparent),
                        title = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Sales Tools",
                                    modifier = Modifier,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelMedium
                                )
//
                                TextButton(onClick = {
                                    filterSheet = true
                                }) {
                                    Row(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .clip(RoundedCornerShape(5.dp))
                                            .background(Color(0xFF04C98B))
                                            .padding(
//                                                horizontal = 6.dp,
                                                8.dp,
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.filtersvg),
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .padding(end = 6.dp)
                                                .size(16.dp),
                                            contentDescription = "filter Icon"
                                        )
                                        Text(
                                            "Filter",
                                            color = MaterialTheme.colorScheme.onSurface,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        },
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = MaterialTheme.colorScheme.background,
                            navigationIconContentColor = Color.Black,
                            titleContentColor = Color.Black,
                            actionIconContentColor = Color.Black,

                            ),
                        scrollBehavior = scrollBehavior
                    )
                }
            },
        ) {
            LazyColumn(
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                if (dataloading == false) {
                    if (policyRatesList.isEmpty()) {
                        item {
                            // Display a message when there are no items
                            Image(
                                painter = painterResource(
                                    R.drawable.not_found),
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
                        // If there are items, display them in the list
                        itemsIndexed(policyRatesList) { index, item ->
                            if (item != null) {
                                //REOPEN
                                PolicyListView(item, index + 1, mainNavController)
                            }
                        }
                    }
                } else {
                    item {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
        if (filterSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    filterSheet = false
                },
                sheetState = sheetState,
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.background
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 4.dp)
                ) {

                    suspend fun <T> safeCall(action: suspend () -> T?): T? {
                        return try {
                            action()
                        } catch (e: Exception) {
                            Log.e("API Error", "Error: ${e.message}")
                            null
                        }
                    }


                    coroutineScope.launch {
                        try {

                            Methods().retrieve_Token(context)?.let { token ->
                                // Sequential calls with safe error handling
                                safeCall { viewModel.getAllVehicleTypes(token) }
                                safeCall { viewModel.getAllVehicleBrands(token) }
                                safeCall { viewModel.getAllVehicleModels(token) }
                                safeCall { viewModel.getAllStates(token) }
                                safeCall { viewModel.getAllFuelTypes(token) }
                                safeCall { viewModel.getAllCities(token) }
                                safeCall { viewModel.getAllInsuranceTypes(token) }
                                safeCall { viewModel.getAllRenewalTypes(token) }
                                safeCall { viewModel.getAllInsurerTypes(token) }
                                safeCall { viewModel.getAllCityCategory(token) }
                            }
                        } catch (e: Exception) {
                            Log.e("Parent Error", "Error occurred: ${e.message}")
                        }
                    }

                    item {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                "Select Vehicle Brand",
                                maxLines = 1,
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            )
                            Spacer(Modifier.padding(6.dp))

                            vehicleBrandTypes?.let { nonNullVehicleBrand ->
                                val selectedValue =
                                    remember { mutableStateOf(mapOf<String, String>()) }

                                VehicleBrandSelection(
                                    vehicleBrand = nonNullVehicleBrand,
                                    selectedValue = vehicleBrandState,
                                    selectionTitle = "VehicleBrand",
                                    onItemSelected = { i ->
                                        selectedValue.value =
                                            mapOf("VehicleBrand" to getItemBrandName(i))
                                    }
                                )
                            }
                            Spacer(Modifier.padding(vertical = 4.dp))

                            Log.d("vehicleBrandState id", vehicleBrandState.value.toString())
                            val modelTypes = vehicleModelTypes?.filter { item ->
                                (vehicleBrandState.value["id"] ?: "") == item.vehicle_brand_id
                            }
                            Log.d("checkValFor model", modelTypes.toString())
                            SelectionView(
                                selectionTitle = "Vehicle Model",
                                staticValue = "Please select a Vehicle Model",
                                selectedValue = vehicleModelState,
                                dropDownViewSelected = vehicleModelDropdownState,
                                listTypes = modelTypes
                            )

// Vehicle Type SelectionView
//                item {
                            Spacer(Modifier.padding(vertical = 4.dp))

                            Text(
                                "Vehicle Type",
                                maxLines = 1,
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            )
                            val selectedValue = remember { mutableStateOf(mapOf<String, String>()) }
                            vehicleTypes?.let { nonNullVehicleTypes ->
                                VehicleTypeSelection(
                                    vehicleTypes = nonNullVehicleTypes,
                                    selectedValue = vehicleTypeState,
                                    selectionTitle = "VehicleType",
                                    onItemSelected = { i ->
                                        selectedValue.value = mapOf("VehicleType" to getItemName(i))
                                    }
                                )
                            }

                            // Fuel Type SelectionView
                            SelectionView(

                                selectionTitle = "Fuel Type",
                                staticValue = "Please select a fuel",
                                selectedValue = fuelTypeState,
                                dropDownViewSelected = fuelTypeDropdownState,
                                listTypes = fuelTypes
                            )

                            // NCB Type SelectionView
                            SelectionView(
                                selectionTitle = "NCB Type",
                                staticValue = "select an NCB Type",
                                selectedValue = ncbState,
                                dropDownViewSelected = ncbDropdownState,
                                listTypes = ncbTypes
                            )

                            // State SelectionView
                            SelectionView(
                                selectionTitle = "State",
                                staticValue = "Please select a state",
                                selectedValue = stateState,
                                dropDownViewSelected = stateDropdownState,
                                listTypes = states
                            )


                            // City Category SelectionView
                            SelectionView(
                                selectionTitle = "City Category",
                                staticValue = "select a city category",
                                selectedValue = cityCategoryState,
                                dropDownViewSelected = cityCategoryDropdownState,
                                listTypes = cityCategories
                            )

                            // City SelectionView


//                val selectedValue = remember { mutableStateOf(mapOf<String, String>()) }
                            Spacer(modifier = Modifier.height(4.dp))
                            val filtercities = cities?.filter { item ->
                                (stateState.value["id"] ?: "") == item.state_id
                            }
                            filtercities?.let { i ->
                                Spacer(Modifier.padding(8.dp))
                                Text(
                                    "Select City",
                                    maxLines = 1,
                                    textAlign = TextAlign.Start,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                )
                                Spacer(Modifier.padding(4.dp))

                                val selectedValue =
                                    remember { mutableStateOf(mapOf<String, String>()) }

                                VehicleCitySelection(
                                    city = i,
                                    selectedValue = cityState,
                                    selectionTitle = "City",
                                    onItemSelected = { i ->
//                            cityState.value =
//                                mapOf("VehicleBrand" to getItemCityName(cityDropdownState))
                                        selectedValue.value =
                                            mapOf("VehicleType" to getItemCityName(i))

                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))

//                            Divider(modifier = Modifier.padding(), color = Color.Black, thickness = 1.dp)
                            // Insurance Type SelectionView
                            SelectionView(
                                selectionTitle = "Insurance Type",
                                staticValue = "select an insurance type",
                                selectedValue = insuranceTypeState,
                                dropDownViewSelected = insuranceTypeDropdownState,
                                listTypes = insuranceTypes
                            )

                            // Renewal Type SelectionView
                            SelectionView(
                                selectionTitle = "Renewal Type",
                                staticValue = "select a renewal type",
                                selectedValue = renewalTypeState,
                                dropDownViewSelected = renewalTypeDropdownState,
                                listTypes = renewalTypes
                            )

                            // Insurer SelectionView
                            SelectionView(
                                selectionTitle = "Insurer",
                                staticValue = "Please select an insurer",
                                selectedValue = insurerState,
                                dropDownViewSelected = insurerDropdownState,
                                listTypes = insurers
                            )
//                }
                        }
                    }


                    item {
                        Row(
                            Modifier.fillMaxWidth(1f),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustBtn(text = "Reset", isSplit = true) {
                                val filterpayload = resetAllStates()


                                coroutineScope.launch {
                                    Methods().retrieve_Token(context)?.let { it1 ->
                                        viewModel.filterPolicyRateData(
                                            it1, filterpayload
                                        )
                                    }
                                }
                                coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        filterSheet = false
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.padding(2.dp))
                            CustBtn(text = "Apply", isblue = true) {
                                val filterpayload = SearchPolicyRatePayload(
                                    state_id = stateState.value["id"] ?: "",
                                    city_id = cityState.value["id"] ?: "",
                                    city_category_id = cityCategoryState.value["id"] ?: "",
                                    vehicle_type_id = vehicleTypeState.value["id"] ?: "",
                                    vehicle_model_id = "",
                                    renewal_type_id = renewalTypeState.value["id"] ?: "",
                                    insurance_type_id = insuranceTypeState.value["id"] ?: "",
                                    insurer_id = insurerState.value["id"] ?: "",
                                    fuel_type_id = fuelTypeState.value["id"] ?: "",
                                    status = if (ncbState.value["name"].isNullOrEmpty()) {
                                        ""
                                    } else {
                                        if (ncbState.value["name"] == "YES") "1" else "0"
                                    },
                                    page = 1,
                                    size = 20
                                )
                                coroutineScope.launch {

                                    val result = Methods().retrieve_Token(context)?.let { it1 ->
                                        viewModel.filterPolicyRateData(
                                            it1, filterpayload
                                        )
                                    }
                                    if (result != null) {
                                        viewModel.updatePolicyRates(result)
                                    }


                                }
                                coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        filterSheet = false
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun createSelectionState(): Pair<MutableState<Map<String, String>>, MutableState<Map<String, Boolean>>> {
    val selectedValue = remember { mutableStateOf(mapOf<String, String>()) }
    val dropDownViewSelected = remember { mutableStateOf(mapOf<String, Boolean>()) }
    return Pair(selectedValue, dropDownViewSelected)
}

@Composable
fun CircularProfileWithWelcome(userName: String) {
    val initials = userName.split(" ").joinToString("") { it.take(1) }.uppercase()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
//            .padding(8.dp)
//            .wrapContentSize() // Optional padding for the row
    ) {
        // Circular Profile Icon
        Box(
            modifier = Modifier
                .size(60.dp) // Size of the circular icon
                .background(Color.Gray, shape = CircleShape) // Circular shape with background color
                .border(2.dp, Color.White, shape = CircleShape) // Optional border
                .padding(18.dp), // Padding inside the circle
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text

        // Welcome Message
        Column {
            Text(
                text = "Welcome",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = userName,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

//
//@SuppressLint("SuspiciousIndentation")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FilterScreen(
//    mainNavController: NavController,
//    viewModel: ApiViewModel,
//    logout: () -> Unit,
//) {
//    val context = LocalContext.current
//    var loading by remember { mutableStateOf(true) }
//    var dataloading by remember { mutableStateOf(true) }
//
//    val policyRatesList by viewModel.getPolicyRates.collectAsState()
//    val vehiclType by viewModel.getVehicleTypes.collectAsState()
//    val vehiclBrand by viewModel.getVehicleBrands.collectAsState()
//    val vehicleModel by viewModel.getVehicleModels.collectAsState()
//    val fuelType by viewModel.getFuelTypes.collectAsState()
//
//    // Location Details
//    val allState by viewModel.getAllStates.collectAsState()
//    val cityCategory by viewModel.getAllCityCategory.collectAsState()
//    val allCities by viewModel.getAllCities.collectAsState()
//
//    // Policy Details
//    val allInsuranceTypes by viewModel.getAllInsuranceTypes.collectAsState()
//    val allRenewalTypes by viewModel.getAllRenewalTypes.collectAsState()
//    val allInsurerTypes by viewModel.getAllInsurerTypes.collectAsState()
//
//    val sheetState =
//        androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true)
//    var filterSheet by remember { mutableStateOf(false) }
//
//    // Initialize states for selections
//    val (vehicleTypeState, vehicleTypeDropdownState) = createSelectionState()
//    val (fuelTypeState, fuelTypeDropdownState) = createSelectionState()
//    val (vehicleBrandState, vehicleBrandDropdownState) = createSelectionState()
//    val (vehicleModelState, vehicleModelDropdownState) = createSelectionState()
//    val (ncbState, ncbDropdownState) = createSelectionState()
//
//    // Location Details
//    val (stateState, stateDropdownState) = createSelectionState()
//    val (cityCategoryState, cityCategoryDropdownState) = createSelectionState()
//    val (cityState, cityDropdownState) = createSelectionState()
//
//    // Policy Details
//    val (insuranceTypeState, insuranceTypeDropdownState) = createSelectionState()
//    val (renewalTypeState, renewalTypeDropdownState) = createSelectionState()
//    val (insurerState, insurerDropdownState) = createSelectionState()
//
//    fun resetAllStates(): SearchPolicyRatePayload {
//        vehicleTypeState.value = mapOf("id" to "", "name" to "")
//        fuelTypeState.value = mapOf("id" to "", "name" to "")
//        ncbState.value = mapOf("name" to "")
//        stateState.value = mapOf("id" to "", "name" to "")
//        cityCategoryState.value = mapOf("id" to "", "name" to "")
//        cityState.value = mapOf("id" to "", "name" to "")
//        insuranceTypeState.value = mapOf("id" to "", "name" to "")
//        renewalTypeState.value = mapOf("id" to "", "name" to "")
//        insurerState.value = mapOf("id" to "", "name" to "")
//
//        return SearchPolicyRatePayload(
//            state_id = stateState.value["id"] ?: "",
//            city_id = cityState.value["id"] ?: "",
//            city_category_id = cityCategoryState.value["id"] ?: "",
//            vehicle_type_id = vehicleTypeState.value["id"] ?: "",
//            vehicle_model_id = "",
//            renewal_type_id = renewalTypeState.value["id"] ?: "",
//            insurance_type_id = insuranceTypeState.value["id"] ?: "",
//            insurer_id = insurerState.value["id"] ?: "",
//            fuel_type_id = fuelTypeState.value["id"] ?: "",
//            status = if (ncbState.value["name"].isNullOrEmpty()) "" else if (ncbState.value["name"] == "YES") "1" else "0",
//            page = 1,
//            size = 20
//        )
//    }
//
//    // Data for various fields
//    val vehicleTypes = vehiclType?.data
//    val fuelTypes = fuelType?.data
//    val vehicleBrandTypes = vehiclBrand?.data
//    val vehicleModelTypes = vehicleModel?.data
//    val ncbTypes = listOf("YES", "NO") // Static for NCB Type
//    val states = allState?.data
//    val cityCategories = cityCategory?.data
//    val cities = allCities?.data
//    val insuranceTypes = allInsuranceTypes?.data
//    val renewalTypes = allRenewalTypes?.data
//    val insurers = allInsurerTypes?.data
//
//    var searchVal by remember { mutableStateOf("") }
//
//    ERPTheme {
//        val coroutineScope = rememberCoroutineScope()
//        val payload = SearchPolicyRatePayload(
//            state_id = "",
//            city_id = "",
//            city_category_id = "",
//            vehicle_type_id = "",
//            vehicle_model_id = "",
//            renewal_type_id = "",
//            insurance_type_id = "",
//            insurer_id = "",
//            fuel_type_id = "",
//            status = "",
//            page = 1,
//            size = 50
//        )
//
//        LaunchedEffect(key1 = true) {
//            coroutineScope.launch {
//                Methods().retrieve_Token(context)?.let {
//                    viewModel.getAllPolicyRates(
//                        token = it,
//                        context = context,
//                        payload = payload,
//                        logout
//                    )
//                }
//                dataloading = false
//            }
//        }
//
//        Scaffold(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background),
//            topBar = {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(MaterialTheme.colorScheme.scrim)
//                ) {
//                    TopAppBar(
//                        modifier = Modifier.background(Color.Transparent),
//                        title = {
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                Text(
//                                    "Sales Tools", color = MaterialTheme.colorScheme.onSurface,
//                                    style = MaterialTheme.typography.labelMedium
//                                )
//                                // Filter Button Implementation Here...
//                                TextButton(onClick = {
//                                    filterSheet = true
//                                }) {
//                                    Row(
//                                        modifier = Modifier
//                                            .wrapContentSize()
//                                            .clip(RoundedCornerShape(5.dp))
//                                            .background(Color(0xFF04C98B))
//                                            .padding(
////                                                horizontal = 6.dp,
//                                                8.dp,
//                                            ),
//                                        verticalAlignment = Alignment.CenterVertically
//                                    ) {
//                                        Icon(
//                                            painter = painterResource(R.drawable.filtersvg),
//                                            tint = MaterialTheme.colorScheme.onSurface,
//                                            modifier = Modifier
//                                                .padding(end = 6.dp)
//                                                .size(16.dp),
//                                            contentDescription = "filter Icon"
//                                        )
//                                        Text(
//                                            "Filter",
//                                            color = MaterialTheme.colorScheme.onSurface,
//                                            style = MaterialTheme.typography.bodySmall
//                                        )
//                                    }
//                                }
//                            }
//
//                        },
//                        colors = TopAppBarDefaults.largeTopAppBarColors(
//                            containerColor = Color.Transparent,
//                            scrolledContainerColor = MaterialTheme.colorScheme.background,
//                            navigationIconContentColor = Color.Black,
//                            titleContentColor = Color.Black,
//                            actionIconContentColor = Color.Black,
//                        )
//                    )
//                }
//            },
//        ) { paddingValues ->
//            LazyColumn(
//                Modifier
//                    .background(MaterialTheme.colorScheme.background)
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .padding(horizontal = 4.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                if (!dataloading) {
//                    if (policyRatesList.isEmpty()) {
//                        item {
//                            Image(
//                                painterResource(R.drawable.not_found),
//                                contentDescription = "Not Found Image"
//                            )
//                            Text(
//                                text = "No Data Available",
//                                textAlign = TextAlign.Center,
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(16.dp),
//                                style = MaterialTheme.typography.labelMedium,
//                                color = MaterialTheme.colorScheme.onSurface
//                            )
//                        }
//                    } else {
//                        itemsIndexed(policyRatesList) { index, item ->
//                            item?.let {
//                                 PolicyListView(it, index + 1, mainNavController)
//                            }
//                        }
//                    }
//                } else {
//                    item {
//                        Box(
//                            Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) { CircularProgressIndicator() }
//                    }
//                }
//            }
//
//            if (filterSheet) {
//                ModalBottomSheet(
//                    onDismissRequest = { filterSheet = false },
//                    sheetState = sheetState,
//                    contentColor = MaterialTheme.colorScheme.onSurface,
//                    containerColor = MaterialTheme.colorScheme.background
//                ) {
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 16.dp, horizontal = 4.dp)
//                    ) {
//                        coroutineScope.launch {
//                            try {
//                                Methods().retrieve_Token(context)?.let { token ->
//                                    // Fetch data concurrently using async...
//
//                                    val vehicleTypesDeferred =
//                                        async { viewModel.getAllVehicleTypes(token) }
//                                    val vehicleBrandsDeferred =
//                                        async { viewModel.getAllVehicleBrands(token) }
//                                    val vehicleModelDeferred =
//                                        async { viewModel.getAllVehicleModels(token) }
//                                    val statesDeferred = async { viewModel.getAllStates(token) }
//                                    val fuelTypesDeferred =
//                                        async { viewModel.getAllFuelTypes(token) }
//                                    val allCitiesDeferred = async { viewModel.getAllCities(token) }
//                                    val allInsuranceTypesDeferred =
//                                        async { viewModel.getAllInsuranceTypes(token) }
//                                    val allRenewalTypesDeferred =
//                                        async { viewModel.getAllRenewalTypes(token) }
//                                    val allInsurerTypesDeferred =
//                                        async { viewModel.getAllInsurerTypes(token) }
//                                    val allCityCategoryDeferred =
//                                        async { viewModel.getAllCityCategory(token) }
//                                    // Await all results
////                        userDeferred.await()
//                                    vehicleTypesDeferred.await()
//                                    vehicleBrandsDeferred.await()
//                                    vehicleModelDeferred.await()
//                                    statesDeferred.await()
//                                    fuelTypesDeferred.await()
//                                    allCitiesDeferred.await()
//                                    allCityCategoryDeferred.await()
//                                    allInsuranceTypesDeferred.await()
//                                    allRenewalTypesDeferred.await()
//                                    allInsurerTypesDeferred.await()
//
//                                    loading = false
//                                }
//                            } catch (e: Exception) {
//                                println("Error occurred: ${e.message}")
//                                loading = false
//                            }
//                        }
//
//                        // Add UI components for filtering here using dataLists...
//                        // Example: Vehicle Brand Selection...
//                        item {
//                            Column(
//                                verticalArrangement = Arrangement.Center,
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//
//                                Text(
//                                    "Select Vehicle Brand",
//                                    maxLines = 1,
//                                    textAlign = TextAlign.Center,
//                                    color = Color(0xFFC4C4C4),
//                                    modifier = Modifier.padding(8.dp)
//                                )
//                                Spacer(Modifier.padding(2.dp))
//
//                                vehicleBrandTypes?.let { nonNullVehicleBrand ->
//                                    val selectedValue =
//                                        remember { mutableStateOf(mapOf<String, String>()) }
//
//                                    VehicleBrandSelection(
//                                        vehicleBrand = nonNullVehicleBrand,
//                                        selectedValue = vehicleBrandState,
//                                        selectionTitle = "VehicleBrand",
//                                        onItemSelected = { i ->
//                                            selectedValue.value =
//                                                mapOf("VehicleBrand" to getItemBrandName(i))
//                                        }
//                                    )
//                                }
//                                Spacer(Modifier.padding(vertical = 4.dp))
//
//                                Log.d("vehicleBrandState id", vehicleBrandState.value.toString())
//                                val modelTypes = vehicleModelTypes?.filter { item ->
//                                    (vehicleBrandState.value["id"] ?: "") == item.vehicle_brand_id
//                                }
//                                Log.d("checkValFor model", modelTypes.toString())
//                                SelectionView(
//                                    selectionTitle = "Vehicle Model",
//                                    staticValue = "Please select a Vehicle Model",
//                                    selectedValue = vehicleModelState,
//                                    dropDownViewSelected = vehicleModelDropdownState,
//                                    listTypes = modelTypes
//                                )
//
//
//// Vehicle Type SelectionView
////                item {
//                                Spacer(Modifier.padding(vertical = 4.dp))
//
//                                Text(
//                                    "Vehicle Type",
//                                    maxLines = 1,
//                                    textAlign = TextAlign.Center,
//                                    color = Color(0xFFC4C4C4)
//                                )
//                                val selectedValue =
//                                    remember { mutableStateOf(mapOf<String, String>()) }
//                                vehicleTypes?.let { nonNullVehicleTypes ->
//                                    VehicleTypeSelection(
//                                        vehicleTypes = nonNullVehicleTypes,
//                                        selectedValue = vehicleTypeState,
//                                        selectionTitle = "VehicleType",
//                                        onItemSelected = { i ->
//                                            selectedValue.value =
//                                                mapOf("VehicleType" to getItemName(i))
//                                        }
//                                    )
//                                }
//
//                                // Fuel Type SelectionView
//                                SelectionView(
//                                    selectionTitle = "Fuel Type",
//                                    staticValue = "Please select a fuel",
//                                    selectedValue = fuelTypeState,
//                                    dropDownViewSelected = fuelTypeDropdownState,
//                                    listTypes = fuelTypes
//                                )
//
//                                // NCB Type SelectionView
//                                SelectionView(
//                                    selectionTitle = "NCB Type",
//                                    staticValue = "select an NCB Type",
//                                    selectedValue = ncbState,
//                                    dropDownViewSelected = ncbDropdownState,
//                                    listTypes = ncbTypes
//                                )
//
//                                // State SelectionView
//                                SelectionView(
//                                    selectionTitle = "State",
//                                    staticValue = "Please select a state",
//                                    selectedValue = stateState,
//                                    dropDownViewSelected = stateDropdownState,
//                                    listTypes = states
//                                )
//
//
//                                // City Category SelectionView
//                                SelectionView(
//                                    selectionTitle = "City Category",
//                                    staticValue = "select a city category",
//                                    selectedValue = cityCategoryState,
//                                    dropDownViewSelected = cityCategoryDropdownState,
//                                    listTypes = cityCategories
//                                )
//
//                                // City SelectionView
//
//                                Spacer(Modifier.padding(8.dp))
//                                Text(
//                                    "Select City",
//                                    maxLines = 1,
//                                    textAlign = TextAlign.Center,
//                                    color = Color(0xFFC4C4C4)
//                                )
////                val selectedValue = remember { mutableStateOf(mapOf<String, String>()) }
//                                Spacer(modifier = Modifier.height(4.dp))
//
//                                cities?.let { i ->
//                                    val selectedValue =
//                                        remember { mutableStateOf(mapOf<String, String>()) }
//
//                                    VehicleCitySelection(
//                                        city = i,
//                                        selectedValue = cityState,
//                                        selectionTitle = "City",
//                                        onItemSelected = { i ->
////                            cityState.value =
////                                mapOf("VehicleBrand" to getItemCityName(cityDropdownState))
//                                            selectedValue.value =
//                                                mapOf("VehicleType" to getItemCityName(i))
//
//                                        }
//                                    )
//                                }
//                                Spacer(modifier = Modifier.height(4.dp))
//
////                            Divider(modifier = Modifier.padding(), color = Color.Black, thickness = 1.dp)
//                                // Insurance Type SelectionView
//                                SelectionView(
//                                    selectionTitle = "Insurance Type",
//                                    staticValue = "select an insurance type",
//                                    selectedValue = insuranceTypeState,
//                                    dropDownViewSelected = insuranceTypeDropdownState,
//                                    listTypes = insuranceTypes
//                                )
//
//                                // Renewal Type SelectionView
//                                SelectionView(
//                                    selectionTitle = "Renewal Type",
//                                    staticValue = "select a renewal type",
//                                    selectedValue = renewalTypeState,
//                                    dropDownViewSelected = renewalTypeDropdownState,
//                                    listTypes = renewalTypes
//                                )
//
//                                // Insurer SelectionView
//                                SelectionView(
//                                    selectionTitle = "Insurer",
//                                    staticValue = "Please select an insurer",
//                                    selectedValue = insurerState,
//                                    dropDownViewSelected = insurerDropdownState,
//                                    listTypes = insurers
//                                )
////                }
//                            }
//                        }
//
//                        item {
//                            Row(
//                                Modifier.fillMaxWidth(1f),
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                CustBtn(text = "Reset", isSplit = true) {
//                                    resetAllStates()
//                                    coroutineScope.launch { /* Call filter function */ }
//                                }
//                                Spacer(modifier = Modifier.padding(2.dp))
//                                CustBtn(text = "Apply", isblue = true) {
//                                    coroutineScope.launch { /* Call apply function */ }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun createSelectionState(): Pair<MutableState<Map<String, String>>, MutableState<Map<String, Boolean>>> {
//    return Pair(
//        remember { mutableStateOf(mapOf<String, String>()) },
//        remember { mutableStateOf(mapOf<String, Boolean>()) })
//}




