package com.example.erp.android.ui.screens.bottomNavScreens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.TextButton
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.erp.android.R
import com.example.erp.android.ui.screens.bottomNavScreens.Camera.FileButton
import com.example.erp.android.ui.screens.bottomNavScreens.Camera.copyUriToFile
import com.example.erp.android.ui.screens.PolicyListView
import com.example.erp.android.ui.screens.SelectionView
import com.example.lms.Services.Dataclass.CityCategoryData
import com.example.lms.Services.Dataclass.CityData
import com.example.lms.Services.Dataclass.FuelTypeData
import com.example.lms.Services.Dataclass.InsuranceTypeData
import com.example.lms.Services.Dataclass.InsurerData
import com.example.lms.Services.Dataclass.PolicyRateData
import com.example.lms.Services.Dataclass.RenewalTypeData
import com.example.lms.Services.Dataclass.SearchPolicyRatePayload
import com.example.lms.Services.Dataclass.StatesData
import com.example.lms.Services.Dataclass.VehicleData
import com.example.erp.android.apiServices.ApiViewModel
import com.example.lms.android.Services.Methods
import com.example.erp.android.ui.component.CustBtn
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun VehicleNumber(context: Context, viewModel: ApiViewModel) {
    CameraPreviewApp(context, viewModel)

//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.onBackground)
//    ) {
//
////        item { CameraPreviewApp(context) }
////        item { FileButton(
////            onFileSelected = {},
////            title = "Select Image",
////        ) }
//
//    }

}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPreviewApp(context: Context, viewModel: ApiViewModel) {

    var enteredVehicleNumber by remember { mutableStateOf("") }

    var vehicle_Type by mutableStateOf<VehicleData?>(null)
    var fuel_Type by mutableStateOf<FuelTypeData?>(null)
    var states_Data by mutableStateOf<StatesData?>(null)
    var city_Categories by mutableStateOf<CityCategoryData?>(null)
    var city_Data by mutableStateOf<CityData?>(null)
    var insurance_Types by mutableStateOf<InsuranceTypeData?>(null)
    var renewal_Types by mutableStateOf<RenewalTypeData?>(null)
    var insurer_Type by mutableStateOf<InsurerData?>(null)

    val coroutineScope = rememberCoroutineScope()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imagePath by remember { mutableStateOf<String?>(null) }
    var filename by remember { mutableStateOf<String?>(null) }
    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val vehicleNumber by viewModel.getRegistrationNumberFromImage.collectAsState() // ViewModel for retrieving vehicle number

    val userData by viewModel.getUserdata.collectAsState()
    val vehiclType by viewModel.getVehicleTypes.collectAsState()
    val fuelType by viewModel.getFuelTypes.collectAsState()
    //location Details
    val allState by viewModel.getAllStates.collectAsState()
    val cityCategory by viewModel.getAllCityCategory.collectAsState()
    val allCities by viewModel.getAllCities.collectAsState()
    //policy Details
    val allInsuranceTypes by viewModel.getAllInsuranceTypes.collectAsState()
    val allRenewalTypes by viewModel.getAllRenewalTypes.collectAsState()
    val allInsurerTypes by viewModel.getAllInsurerTypes.collectAsState()

    // Assuming getVehicleDetails is provided by some data source
    val getVehicleDetails by viewModel.getVehicleDetails.collectAsState()



    // Data for various fields
    val vehicleTypes = vehiclType?.data
    val fuelTypes = fuelType?.data
    val ncbTypes = listOf("YES", "NO")  // Static for NCB Type
    val states = allState?.data
    val cityCategories = cityCategory?.data
    val cities = allCities?.data
    val insuranceTypes = allInsuranceTypes?.data
    val renewalTypes = allRenewalTypes?.data
    val insurers = allInsurerTypes?.data

    var uploadPictureLoader by remember { mutableStateOf(false) } // Loader for vehicle number submission
    var vehicleNumberLoader by remember { mutableStateOf(false) } // Loader for vehicle number submission
    var policyRateLoader by remember { mutableStateOf(false) } // Loader for policy rates retrieval

    // Vehicle Details
    val (vehicleTypeState, vehicleTypeDropdownState) = createSelectionState()
    val (fuelTypeState, fuelTypeDropdownState) = createSelectionState()
    val (ncbState, ncbDropdownState) = createSelectionState()

    // Location Details
    val (stateState, stateDropdownState) = createSelectionState()
    val (cityCategoryState, cityCategoryDropdownState) = createSelectionState()
    val (cityState, cityDropdownState) = createSelectionState()

    // Policy Details
    val (insuranceTypeState, insuranceTypeDropdownState) = createSelectionState()
    val (renewalTypeState, renewalTypeDropdownState) = createSelectionState()
    val (insurerState, insurerDropdownState) = createSelectionState()

    var xyzdata = remember { mutableListOf<PolicyRateData?>() }


    val snackbarHostState =
        remember { SnackbarHostState() } // Snackbar host state to display messages

    // Function to create an empty file to store the captured image
    val createImageFile: () -> File = {
        val storageDir: File? = context.getExternalFilesDir("Pictures")
        File.createTempFile(
            "JPEG_${System.currentTimeMillis()}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    // Launch camera to take a picture and save to the given Uri
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            imageUri?.let { uri ->
                coroutineScope.launch {
                    imageBitmap =
                        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    val tempFile = copyUriToFile(context, uri)
                    imagePath = tempFile?.absolutePath
                }
            }
        }
    }

    // Launcher for requesting camera permission
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, proceed to take a picture
            val imageFile = createImageFile() // Your method to create image file
            imageUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
            imageUri?.let { uri -> takePictureLauncher.launch(uri) }
        } else {
            // Show a message to the user indicating that permission is required
            Toast.makeText(
                context,
                "Camera permission is required to take pictures.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //Filter api to get the values in chips




    fun loadFuelType(accessModel: ApiViewModel) {
        // Get the fuel type from vehicle details
        val fuelType = getVehicleDetails?.result?.fuel_descr?.uppercase()
        Log.d("fuelType from submit resp:", fuelType.toString())

        // Retrieve the list of fuel types
        val fuelTypeDataList = accessModel.getFuelTypes.value?.data

        Log.d("fuelTypeDataList resp:", fuelTypeDataList.toString())
        val fuel = fuelTypeDataList?.firstOrNull { it.name.uppercase().equals(fuelType, ignoreCase = true) }


        fuel?.name?.let { Log.d("fuelType Value:", it) }

        if (fuel != null) {
            fuel_Type = fuel // Assuming you have a variable to hold selected fuel type
            fuelTypeState.value = mapOf(
                "id" to fuel.id,
                "name" to fuel.name
            ).toMutableMap().apply {
                put("Fuel Type", fuel.name)
            }
        } else {
            fuel_Type = null // Optionally reset or clear fuel_Type
            fuelTypeState.value = emptyMap() // Clear or reset the selection state
        }
    }

    fun loadStatesData(accessModel: ApiViewModel) {
        // Get the state code from vehicle details
        val stateCode = getVehicleDetails?.result?.state_code
        Log.d("stateCode from submit resp:", stateCode.toString())

        // Retrieve the list of states
        val statesDataList = accessModel.getAllStates.value?.data
        Log.d("statesDataList resp:", statesDataList.toString())

        // Find the matching state
        val state = statesDataList?.firstOrNull { it.name.equals(stateCode, ignoreCase = true) }
        state?.name?.let { Log.d("stateCode Value:", it) }

        if (state != null) {
            states_Data = state // Set the found state to states_Data
            // Update the selection state with the state's name
            stateState.value = mapOf(
                "id" to state.id,
                "name" to state.name)
                .toMutableMap().apply {
                    put("State", state.name)
                }
        } else {
            // Handle case where no matching state is found
            states_Data = null // Optionally reset or clear states_Data
            stateState.value = emptyMap() // Clear or reset the selection state
        }
    }

    fun loadInsurerType(accessModel: ApiViewModel) {
        // Get the insurer name from vehicle details
        val insurerName = getVehicleDetails?.result?.vehicle_insurance_details?.insurance_company_name
        Log.d("insurerName from submit resp:", insurerName.toString())

        // Retrieve the list of insurer types
        val insurerTypeDataList = accessModel.getAllInsurerTypes.value?.data
        Log.d("insurerTypeDataList resp:", insurerTypeDataList.toString())

        // Find the matching insurer type
        val insurer = insurerTypeDataList?.firstOrNull { it.name.equals(insurerName, ignoreCase = true) }

        if (insurer != null) {
            insurer_Type = insurer // Set the found insurer type to insurer_Type
            // Update the selection state with the insurer's name
            insurerState.value =mapOf(
                "id" to insurer.id,
                "name" to insurer.name)
                .toMutableMap().apply {
                    put("Insurer", insurer.name)
                }
        } else {
            // Handle case where no matching insurer type is found
            insurer_Type = null // Optionally reset or clear insurer_Type
            insurerState.value = emptyMap() // Clear or reset the selection state
        }
    }

    fun loadVehicleType(accessModel: ApiViewModel) {
        // Get the vehicle type from vehicle details
        val vehicleType = getVehicleDetails?.result?.vehicle_type
        Log.d("vehicleType from submit resp:", vehicleType.toString())

        // Retrieve the list of vehicle types
        val vehicleTypeDataList = accessModel.getVehicleTypes.value?.data

        Log.d("vehicleTypeDataList resp:", vehicleTypeDataList.toString())

        // Find the matching vehicle type
        val vehicle = vehicleTypeDataList?.firstOrNull { it.name.equals(vehicleType, ignoreCase = true) }

        // Log the found vehicle type name if it exists
        vehicle?.name?.let { Log.d("vehicleType Value:", it) }

        if (vehicle != null) {
            // Set the found vehicle type to states_Data or any appropriate variable
            vehicle_Type = vehicle // Assuming you have a variable to hold selected vehicle type

            // Update the selection state with the vehicle type's name
            vehicleTypeState.value = mapOf("name" to vehicle.name)

            Log.d("value func",vehicleTypeState.toString())
            vehicleTypeState.value = mapOf(
                "id" to vehicle.id,
                "name" to vehicle.name
            ).toMutableMap().apply {
                put("Vehicle Type", vehicle.name)
            }

        } else {
            // Handle case where no matching vehicle type is found
            vehicle_Type = null // Optionally reset or clear vehicle_Type
            vehicleTypeState.value = emptyMap() // Clear or reset the selection state
        }
    }

    fun loadCities(accessModel: ApiViewModel) {
        // Get the permanent district name from vehicle details
        val districtName = getVehicleDetails?.result?.permanent_district_name?.lowercase()
        Log.d("districtName from submit resp:", districtName.toString())

        // Retrieve the list of cities
        val citiesDataList = accessModel.getAllCities.value?.data
        Log.d("citiesDataList resp:", citiesDataList.toString())

        // Find the matching city
        val city = citiesDataList?.firstOrNull { it.name.lowercase().equals(districtName, ignoreCase = true) }

        if (city != null) {
            city_Data = city // Set the found city to city_Data

            // Update the selection state with the city's name
            cityState.value = mapOf(
                "id" to city.id,
                "name" to city.name)
                .toMutableMap().apply {
                    put("City", city.name)
                }
        } else {
            // Handle case where no matching city is found
            city_Data = null // Optionally reset or clear city_Data
            cityState.value = emptyMap() // Clear or reset the selection state
        }
    }

    fun loadCityCategories(accessModel: ApiViewModel){
        // Get city category ID from selected city data if available
        val cityCategoryId = city_Data?.city_category_id
//            ?.city_category_id?.lowercase()
        Log.d("cityCategoryId submit resp:", cityCategoryId.toString())

        // Retrieve list of city categories
        val cityCategoryDataList = accessModel.getAllCityCategory.value?.data
        Log.d("cityCategoryDataList resp:", cityCategoryDataList.toString())

        // Find matching city category by ID
        val cityCategory = cityCategoryDataList?.firstOrNull { it.id.lowercase().equals(cityCategoryId, ignoreCase = true) }

        if (cityCategory != null) {
            city_Categories = cityCategory  // Set found category to variable

            // Update selection state with category's name
            cityCategoryState.value = mapOf(
                "id" to cityCategory.id,
                "name" to cityCategory.name)
                .toMutableMap().apply {
                    put("City Category", cityCategory.name)
                }
        } else {
            // Handle case where no matching category is found
            city_Categories = null  // Optionally reset or clear category variable
            cityCategoryState.value = emptyMap()  // Clear or reset selection state
        }
    }

    fun loadRenewalType(accessModel: ApiViewModel) {
        val insuranceDetails = getVehicleDetails?.result?.vehicle_insurance_details
        var renewalTypeName = ""

        insuranceDetails?.insurance_from?.let { insuranceStartDateString ->
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val insuranceStartDate = dateFormatter.parse(insuranceStartDateString)
            val currentDate = Date()

            insuranceStartDate?.let {
                val timeDiff = currentDate.time - insuranceStartDate.time
                val daysDiff = timeDiff / (1000 * 60 * 60 * 24)

                renewalTypeName = when {
                    daysDiff < 30 -> "Rollover"
                    daysDiff in 30..365 -> "New"
                    else -> "Brand New"
                }

                val matchedRenewalType = accessModel.getAllRenewalTypes.value?.data
                    ?.firstOrNull { it.name.equals(renewalTypeName, ignoreCase = true) }
                renewal_Types = matchedRenewalType

                renewal_Types?.let {
                    renewalTypeState.value = mapOf("name" to it.name) // Assuming you want to store it in a map
                }
            }
        }
    }


    fun createSelection_State(): Pair<MutableState<Any?>, MutableState<Boolean>> {
        val insurerState = mutableStateOf<Any?>(null) // Use Any instead of InsurerData
        val insurerDropdownState = mutableStateOf(false)
        return Pair(insurerState, insurerDropdownState)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Camera Preview with Path") })
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onBackground)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display captured image
                // Display captured image
                item {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap!!.asImageBitmap(),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .fillMaxHeight(0.4f)
                                .fillMaxWidth(0.6f),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_person_outline_24),
                            contentDescription = "Placeholder Image",
                            modifier = Modifier
                                .fillMaxHeight(0.4f)
                                .fillMaxWidth(0.6f),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                // Display image path if available
                imagePath?.let {
                    item {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "Image path: $it",
                            color = Color.Black
                        )

                    }
                }

                // Button to capture a new picture
                item {
                    TextButton(onClick = {
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED -> {
                                // Permission already granted, proceed to take a picture
                                val imageFile =
                                    createImageFile() // Your method to create image file
                                imageUri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    imageFile
                                )
                                imageUri?.let { uri -> takePictureLauncher.launch(uri) }
                            }

                            else -> {
                                // Request camera permission
                                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }) {
                        Text(text = "Take Picture", color = Color.Blue)
                    }
                }

                item {
                    Text(
                        text = "OR",
                        color = Color.Black,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                // Button to select an image from the file system
                item {
                    FileButton(
                        onFileSelected = { uri ->
                            imageUri = uri
                            coroutineScope.launch {
                                imageBitmap = MediaStore.Images.Media.getBitmap(
                                    context.contentResolver,
                                    uri
                                )
                                val tempFile = copyUriToFile(context, uri)
                                imagePath = tempFile?.absolutePath
                                filename = tempFile?.name
                            }
                        },
                        title = "Select Image"
                    )
                }
// Button to upload the selected image
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        if (!uploadPictureLoader) {
                            CustBtn(text = "Upload Image", isblue = true) {
                                coroutineScope.launch {
                                    uploadPictureLoader = true
                                    val result = Methods().retrieve_Token(context)?.let { token ->
                                        imagePath?.let { path ->
//                                        loader = true // Display loader during image upload
                                            viewModel.uploadImage(token, path)
//                                        loader = false
                                        }
                                    }
                                    if (result.isNullOrBlank()) {
                                        Toast.makeText(
                                            context,
                                            "failed to fetch",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Fetched Successfully", Toast.LENGTH_SHORT
                                        ).show()

                                        enteredVehicleNumber = result
                                    }
                                    uploadPictureLoader = false
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ){
                                CircularProgressIndicator(
                                    color = Color.Blue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
// Display vehicle number retrieved from image if available
                item {


                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Enter Vehicle Number",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.gilroy_semibold)),
                                fontSize = 22.sp,
                                color = Color(0xFF4E4E4E)
                            )
                        )

                        OutlinedTextField(
                            value = enteredVehicleNumber,
                            onValueChange = { enteredVehicleNumber = it },
                            label = { Text("Vehicle Number") },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            vehicleNumberLoader = true
                                            val result = Methods().retrieve_Token(context)
                                                ?.let {
                                                    enteredVehicleNumber.let { it1 ->
                                                        viewModel.getVehicleDetails(
                                                            it,
                                                            it1
                                                        )
                                                    }
                                                }
                                            vehicleNumberLoader = false
                                            if (result == "success") {
                                                delay(2000)
                                                loadStatesData(viewModel)
                                                loadFuelType(viewModel)
                                                loadCities(viewModel)
                                                loadInsurerType(viewModel)
                                                loadRenewalType(viewModel)
                                                loadVehicleType(viewModel)
                                                loadCityCategories(viewModel)
                                                delay(1000)

                                                val payload = SearchPolicyRatePayload(
                                                    state_id = states_Data?.id ?: "",
                                                    city_id = city_Data?.id ?: "",
                                                    city_category_id = city_Categories?.id
                                                        ?: "",
                                                    vehicle_type_id = vehicle_Type?.id ?: "",
                                                    renewal_type_id = renewal_Types?.id ?: "",
                                                    insurer_id = insurer_Type?.id ?: "",
                                                    fuel_type_id = fuel_Type?.id ?: "",
                                                    insurance_type_id = "",
                                                    vehicle_model_id = "",
                                                    status = "0",
                                                    page = 1,
                                                    size = 50
                                                )
                                                xyzdata = (Methods().retrieve_Token(context)
                                                    ?.let {
                                                        viewModel.filterPolicyRateData(
                                                            it,
                                                            payload
                                                        )
                                                    }?.toMutableList()
                                                    ?: emptyList()).toMutableList()
                                                if (xyzdata.isEmpty()) {
                                                    Toast.makeText(
                                                        context,
                                                        "No Policy Data Found ",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }


                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "No Record found For Vehicle Number",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            }
                                            Log.d("getVehicleDetails", result.toString())
                                        }
                                    },
                                    modifier = Modifier
                                        .height(45.dp)
                                        .width(100.dp),
                                    enabled = !vehicleNumberLoader
                                ) {
                                    if (vehicleNumberLoader) {
                                        CircularProgressIndicator(
                                            color = Color.Blue,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "Submit",
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.gilroy_semibold)),
                                                fontSize = 16.sp,
                                                color = Color.White
                                            )
                                        )
                                    }
                                }
                            }
                        )
                        if (enteredVehicleNumber.isNotEmpty()) {
                            Text(
                                text = "Please verify the vehicle number before continuing",
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.gilroy_semibold)),
                                    fontSize = 16.sp,
                                    color = Color.Blue
                                ),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        Divider()
                        if (policyRateLoader) {
                            CircularProgressIndicator()
                        }

                    }

                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
// Vehicle Type SelectionView
                        item {
                            SelectionView(
                                selectionTitle = "Vehicle Type",
                                staticValue = "Please select a vehicle",
                                selectedValue = vehicleTypeState,
                                dropDownViewSelected = vehicleTypeDropdownState,
                                listTypes = vehicleTypes
                            )

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
                            SelectionView(
                                selectionTitle = "City",
                                staticValue = "Please select a city",
                                selectedValue = cityState,
                                dropDownViewSelected = cityDropdownState,
                                listTypes = cities
                            )
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
                        }
                    }
                }

                if (xyzdata.isEmpty()) {
                    item {
                        // Display a message when there are no items

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
                    itemsIndexed(xyzdata) { index, item ->
                        if (item != null) {
                            PolicyListView(item, index + 1)
                        }
                    }
                }

            }

        }

    )

}

@Composable
fun VehicleNumberSubmissionSection(
    vehicleNumber: String,
    onSubmit: (String) -> Unit,
    vehicleNumberLoader: Boolean,
    snackbarHostState: SnackbarHostState,
    policyRateLoader: Boolean,
    viewModel: ApiViewModel
) {
    var enteredVehicleNumber by remember { mutableStateOf(vehicleNumber) }

}

