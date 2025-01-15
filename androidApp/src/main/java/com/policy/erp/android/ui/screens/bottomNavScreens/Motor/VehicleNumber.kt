package com.policy.erp.android.ui.screens.bottomNavScreens.Motor

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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.policy.erp.android.R
import com.policy.erp.android.apiServices.ApiViewModel
import com.policy.erp.android.ui.bottombarGraph.BottomBarScreen
import com.policy.erp.android.ui.component.CustBtn
import com.policy.erp.android.ui.screens.SelectionView
import com.policy.erp.android.ui.screens.bottomNavScreens.Camera.FileButton
import com.policy.erp.android.ui.screens.bottomNavScreens.Camera.copyUriToFile
import com.policy.erp.android.ui.screens.bottomNavScreens.DetailTabData
import com.policy.erp.android.ui.screens.bottomNavScreens.DetailsTabbedView
import com.policy.erp.android.ui.screens.bottomNavScreens.createSelectionState
import com.policy.lms.Services.Dataclass.BrandData
import com.policy.lms.Services.Dataclass.CityCategoryData
import com.policy.lms.Services.Dataclass.CityData
import com.policy.lms.Services.Dataclass.FuelTypeData
import com.policy.lms.Services.Dataclass.InsuranceTypeData
import com.policy.lms.Services.Dataclass.InsurerData
import com.policy.lms.Services.Dataclass.PolicyRateData
import com.policy.lms.Services.Dataclass.RenewalTypeData
import com.policy.lms.Services.Dataclass.SearchPolicyRatePayload
import com.policy.lms.Services.Dataclass.StatesData
import com.policy.lms.Services.Dataclass.VehicleBrand
import com.policy.lms.Services.Dataclass.VehicleData
import com.policy.lms.android.Services.Methods
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun VehicleNumber(context: Context, viewModel: ApiViewModel, mainNavController: NavHostController) {
    CameraPreviewApp(context, viewModel, mainNavController)
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPreviewApp(
    context: Context,
    viewModel: ApiViewModel,
    mainNavController: NavHostController,
) {


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
                                painter = painterResource(R.drawable.motor),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "motor Icon"
                            )
                            Spacer(Modifier.padding(2.dp))
                            Text(
                                "Motor",
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
        content = { paddingValues ->
            Column(Modifier.padding(paddingValues))
            {
                TabScreen(context, viewModel, mainNavController)

            }
        }
    )


}

@Composable
fun UploadImageContent(
    context: Context,
    viewModel: ApiViewModel,
    navigateToManualTab: () -> Unit
) {
    var enteredVehicleNumber by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imagePath by remember { mutableStateOf<String?>(null) }
    var filename by remember { mutableStateOf<String?>(null) }
    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val vehicleNumber by viewModel.getRegistrationNumberFromImage.collectAsState() // ViewModel for retrieving vehicle number
    val coroutineScope = rememberCoroutineScope()
    var uploadPictureLoader by remember { mutableStateOf(false) } // Loader for vehicle number submission

    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 1f)
    )

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

    val scrollSheet = rememberScrollState()

    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer,
        ),
        start = Offset(0f, 90f),
        end = Offset(0f, 1800f)
    )
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .scrollable(scrollSheet, orientation = Orientation.Vertical),
    ) {
        Box(
            Modifier
                .weight(0.6f)
                .clickable {
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
                }
                .padding(8.dp)
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(color = Color(0xFF797979), style = stroke)
                }
                .clip(RoundedCornerShape(6.dp))

                .weight(0.4f)
                .background(gradient)
                .clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.capture__2_),
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Capture Image"
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = "Open Camera to Capture Image",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(Modifier.padding(6.dp))

//                }
//                item{
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .background(MaterialTheme.colorScheme.scrim)
                .padding(8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "or you can simply upload from\n" +
                            "Your Gallery ",
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

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
//                }
//                item{
        Spacer(Modifier.padding(8.dp))
        Card(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(0.6f)
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.dummy_image),
                    contentDescription = "Placeholder Image",
                    modifier = Modifier
                        .padding(80.dp)
                        .fillMaxSize(1f),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Spacer(Modifier.padding(8.dp))

        if (!uploadPictureLoader) {
            CustBtn(text = "Submit", isblue = true, isEnable = imageBitmap != null) {
                coroutineScope.launch {
                    uploadPictureLoader = true
                    val result =
                        Methods().retrieve_Token(context)?.let { token ->
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
                        navigateToManualTab()
                    } else {
                        Toast.makeText(
                            context,
                            "Fetched Successfully", Toast.LENGTH_SHORT
                        ).show()
                        navigateToManualTab()
                        enteredVehicleNumber = result
                    }
                    uploadPictureLoader = false
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Blue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }


    }
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


@Composable
fun TabScreen(context: Context, viewModel: ApiViewModel, mainNavController: NavHostController) {


    var selectedTab by remember { mutableStateOf<DetailTabData?>(null) }

    // Create the "Manual" tab first so it can be referenced in the "Upload Image" tab
    val manualTab = remember {
        DetailTabData(
            title = "Manual",
            action = { /* Additional logic if needed */ },
            content = { ManualContent(viewModel, context, mainNavController) }
        )
    }

    // Create the "Upload Image" tab and pass a lambda to navigate to the "Manual" tab
    val uploadImageTab = remember {
        DetailTabData(
            title = "Upload Image",
            action = { /* Additional logic if needed */ },
            content = {
                UploadImageContent(context, viewModel) {
                    selectedTab = manualTab
                }
            }
        )
    }

    // List of tabs
    val tabItems = listOf(uploadImageTab, manualTab)

    // Initialize the selected tab if not already set
    selectedTab = selectedTab ?: tabItems.first()

    DetailsTabbedView(
        tabItems = tabItems,
        selectedTab = selectedTab!!,
        onTabSelected = { tab -> selectedTab = tab }
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ManualContent(viewModel: ApiViewModel, context: Context, mainNavController: NavHostController) {
    var vehicleNumberLoader by remember { mutableStateOf(false) }
    var enteredVehicleNumber by remember { mutableStateOf("") }
    var policyRateLoader by remember { mutableStateOf(false) } // Loader for policy rates retrieval
    val coroutineScope = rememberCoroutineScope()

    var xyzdata = remember { mutableListOf<PolicyRateData?>() }



    // Assuming getVehicleDetails is provided by some data source
    val getVehicleDetails by viewModel.getVehicleDetails.collectAsState()

    // Vehicle Details
    val (vehicleTypeState, vehicleTypeDropdownState) = createSelectionState()
    val (vehicleBrandState, vehicleBrandDropdownState) = createSelectionState()
    val (vehicleModelState, vehicleModelDropdownState) = createSelectionState()
    val (fuelTypeState, fuelTypeDropdownState) = createSelectionState()
    val (ncbState, ncbDropdownState) = createSelectionState()
    // Policy Details
    val (insuranceTypeState, insuranceTypeDropdownState) = createSelectionState()
    val (renewalTypeState, renewalTypeDropdownState) = createSelectionState()
    val (insurerState, insurerDropdownState) = createSelectionState()
    // Location Details
    val (stateState, stateDropdownState) = createSelectionState()
    val (cityCategoryState, cityCategoryDropdownState) = createSelectionState()
    val (cityState, cityDropdownState) = createSelectionState()

    //for filter data collection
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


    var vehicle_Type by mutableStateOf<VehicleData?>(null)
    var vehicle_Brand by mutableStateOf<VehicleBrand?>(null)
    var fuel_Type by mutableStateOf<FuelTypeData?>(null)
    var states_Data by mutableStateOf<StatesData?>(null)
    var city_Categories by mutableStateOf<CityCategoryData?>(null)
    var city_Data by mutableStateOf<CityData?>(null)
    var insurance_Types by mutableStateOf<InsuranceTypeData?>(null)
    var renewal_Types by mutableStateOf<RenewalTypeData?>(null)
    var insurer_Type by mutableStateOf<InsurerData?>(null)




    val vehicleTypes = vehiclType?.data
    val vehicleBrandTypes = vehiclBrand?.data
    val vehicleModelTypes = vehicleModel?.data
    val fuelTypes = fuelType?.data
    val ncbTypes = listOf("YES", "NO")  // Static for NCB Type
    val states = allState?.data
    val cityCategories = cityCategory?.data
    val cities = allCities?.data
    val insuranceTypes = allInsuranceTypes?.data
    val renewalTypes = allRenewalTypes?.data
    val insurers = allInsurerTypes?.data


    fun loadFuelType(accessModel: ApiViewModel) {
        // Get the fuel type from vehicle details
        val fuelType = getVehicleDetails?.result?.fuel_descr?.uppercase()
        Log.d("fuelType from submit resp:", fuelType.toString())

        // Retrieve the list of fuel types
        val fuelTypeDataList = accessModel.getFuelTypes.value?.data

        Log.d("fuelTypeDataList resp:", fuelTypeDataList.toString())
        val fuel = fuelTypeDataList?.firstOrNull {
            it.name.uppercase().equals(fuelType, ignoreCase = true)
        }


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
                "name" to state.name
            )
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
        val insurerName =
            getVehicleDetails?.result?.vehicle_insurance_details?.insurance_company_name
        Log.d("insurerName from submit resp:", insurerName.toString())

        // Retrieve the list of insurer types
        val insurerTypeDataList = accessModel.getAllInsurerTypes.value?.data
        Log.d("insurerTypeDataList resp:", insurerTypeDataList.toString())

        // Find the matching insurer type
        val insurer =
            insurerTypeDataList?.firstOrNull { it.name.equals(insurerName, ignoreCase = true) }

        if (insurer != null) {
            insurer_Type = insurer // Set the found insurer type to insurer_Type
            // Update the selection state with the insurer's name
            insurerState.value = mapOf(
                "id" to insurer.id,
                "name" to insurer.name
            )
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
        val vehicle =
            vehicleTypeDataList?.firstOrNull { it.name.equals(vehicleType, ignoreCase = true) }

        // Log the found vehicle type name if it exists
        vehicle?.name?.let { Log.d("vehicleType Value:", it) }

        if (vehicle != null) {
            // Set the found vehicle type to states_Data or any appropriate variable
            vehicle_Type = vehicle // Assuming you have a variable to hold selected vehicle type

            // Update the selection state with the vehicle type's name
            vehicleTypeState.value = mapOf("name" to vehicle.name)

            Log.d("value func", vehicleTypeState.toString())
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



//    fun loadVehicleBrand(accessModel: ApiViewModel) {
//        // Get the vehicle type from vehicle details
//        val vehicleBrand = getVehicleDetails?.result?.vehicle_type
//        Log.d("vehicleBrand from submit resp:", vehicleType.toString())
//
//        // Retrieve the list of vehicle types
//        val vehicleTypeDataList = accessModel.getVehicleTypes.value?.data
//
//        Log.d("vehicleTypeDataList resp:", vehicleTypeDataList.toString())
//
//        // Find the matching vehicle type
//        val vehicle =
//            vehicleTypeDataList?.firstOrNull { it.name.equals(vehicleType, ignoreCase = true) }
//
//        // Log the found vehicle type name if it exists
//        vehicle?.name?.let { Log.d("vehicleType Value:", it) }
//
//        if (vehicle != null) {
//            // Set the found vehicle type to states_Data or any appropriate variable
//            vehicle_Type = vehicle // Assuming you have a variable to hold selected vehicle type
//
//            // Update the selection state with the vehicle type's name
//            vehicleTypeState.value = mapOf("name" to vehicle.name)
//
//            Log.d("value func", vehicleTypeState.toString())
//            vehicleTypeState.value = mapOf(
//                "id" to vehicle.id,
//                "name" to vehicle.name
//            ).toMutableMap().apply {
//                put("Vehicle Type", vehicle.name)
//            }
//
//        } else {
//            // Handle case where no matching vehicle type is found
//            vehicle_Type = null // Optionally reset or clear vehicle_Type
//            vehicleTypeState.value = emptyMap() // Clear or reset the selection state
//        }
//    }

    fun loadCities(accessModel: ApiViewModel) {
        // Get the permanent district name from vehicle details
        val districtName = getVehicleDetails?.result?.permanent_district_name?.lowercase()
        Log.d("districtName from submit resp:", districtName.toString())

        // Retrieve the list of cities
        val citiesDataList = accessModel.getAllCities.value?.data
        Log.d("citiesDataList resp:", citiesDataList.toString())

        // Find the matching city
        val city = citiesDataList?.firstOrNull {
            it.name.lowercase().equals(districtName, ignoreCase = true)
        }

        if (city != null) {
            city_Data = city // Set the found city to city_Data

            // Update the selection state with the city's name
            cityState.value = mapOf(
                "id" to city.id,
                "name" to city.name
            )
                .toMutableMap().apply {
                    put("City", city.name)
                }
        } else {
            // Handle case where no matching city is found
            city_Data = null // Optionally reset or clear city_Data
            cityState.value = emptyMap() // Clear or reset the selection state
        }
    }

    fun loadCityCategories(accessModel: ApiViewModel) {
        // Get city category ID from selected city data if available
        val cityCategoryId = city_Data?.city_category_id
//            ?.city_category_id?.lowercase()
        Log.d("cityCategoryId submit resp:", cityCategoryId.toString())

        // Retrieve list of city categories
        val cityCategoryDataList = accessModel.getAllCityCategory.value?.data
        Log.d("cityCategoryDataList resp:", cityCategoryDataList.toString())

        // Find matching city category by ID
        val cityCategory = cityCategoryDataList?.firstOrNull {
            it.id.lowercase().equals(cityCategoryId, ignoreCase = true)
        }

        if (cityCategory != null) {
            city_Categories = cityCategory  // Set found category to variable

            // Update selection state with category's name
            cityCategoryState.value = mapOf(
                "id" to cityCategory.id,
                "name" to cityCategory.name
            )
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
                    renewalTypeState.value =
                        mapOf("name" to it.name) // Assuming you want to store it in a map
                }
            }
        }
    }

    LazyColumn {
        coroutineScope.launch {
            try {
                Methods().retrieve_Token(context)?.let { token ->
                    // Use async to call multiple suspend functions concurrently
                    val vehicleTypesDeferred =
                        async { viewModel.getAllVehicleTypes(token) }
                    val vehicleBrandsDeferred =
                        async { viewModel.getAllVehicleBrands(token) }
                    val vehicleModelDeferred =
                        async { viewModel.getAllVehicleModels(token) }
                    val statesDeferred = async { viewModel.getAllStates(token) }
                    val fuelTypesDeferred = async { viewModel.getAllFuelTypes(token) }
                    val allCitiesDeferred = async { viewModel.getAllCities(token) }
                    val allInsuranceTypesDeferred =
                        async { viewModel.getAllInsuranceTypes(token) }
                    val allRenewalTypesDeferred =
                        async { viewModel.getAllRenewalTypes(token) }
                    val allInsurerTypesDeferred =
                        async { viewModel.getAllInsurerTypes(token) }
                    val allCityCategoryDeferred =
                        async { viewModel.getAllCityCategory(token) }
                    // Await all results
//                        userDeferred.await()
                    vehicleTypesDeferred.await()
                    vehicleBrandsDeferred.await()
                    vehicleModelDeferred.await()
                    statesDeferred.await()
                    fuelTypesDeferred.await()
                    allCitiesDeferred.await()
                    allCityCategoryDeferred.await()
                    allInsuranceTypesDeferred.await()
                    allRenewalTypesDeferred.await()
                    allInsurerTypesDeferred.await()


                }
            } catch (e: Exception) {
                println("Error occurred: ${e.message}")
//                loading = false // Handle loading state on error
            }
        }

        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Enter Vehicle Number",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.gilroy_semibold)),
                        fontSize = 22.sp,
                        color = Color(0xFF4E4E4E)
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedTextField(
                        value = enteredVehicleNumber,
                        onValueChange = { enteredVehicleNumber = it },
                        label = { },
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
                    Spacer(Modifier.padding(2.dp))
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
                                        vehicleNumberLoader = false
                                    }


                                } else {
                                    Toast.makeText(
                                        context,
                                        result,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                Log.d("getVehicleDetails", result.toString())
                            }
                        },
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(6.dp),
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
                                maxLines = 1,
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.gilroy_semibold)),
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
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
                if (policyRateLoader) {
                    CircularProgressIndicator()
                }

            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "Select Vehicle Brand",
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFC4C4C4),
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(Modifier.padding(2.dp))

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

                val modelTypes = vehicleModelTypes?.filter { item ->
                    (vehicleBrandState.value["id"] ?: "") == item.vehicle_brand_id
                }

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
                    textAlign = TextAlign.Center,
                    color = Color(0xFFC4C4C4)
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
                if (states != null) {
                    SelectionView(
                        selectionTitle = "State",
                        staticValue = "Please select a state",
                        selectedValue = stateState,
                        dropDownViewSelected = stateDropdownState,
                        listTypes = states.sortedBy { i-> i.name }
                    )
                }


                // City Category SelectionView
                SelectionView(
                    selectionTitle = "City Category",
                    staticValue = "select a city category",
                    selectedValue = cityCategoryState,
                    dropDownViewSelected = cityCategoryDropdownState,
                    listTypes = cityCategories
                )

                // City SelectionView

                Spacer(Modifier.padding(8.dp))
                Text(
                    "Select City",
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFC4C4C4)
                )
//                val selectedValue = remember { mutableStateOf(mapOf<String, String>()) }
                Spacer(modifier = Modifier.height(4.dp))

                val filtercities = cities?.filter { item ->
                    (stateState.value["id"] ?: "") == item.state_id
                }
                filtercities?.let { i ->
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
            CustBtn(text = "Submit", isblue = true) {
                val filterpayload = SearchPolicyRatePayload(
                    state_id = stateState.value["id"] ?: "",
                    city_id = cityState.value["id"] ?: "",
                    city_category_id = cityCategoryState.value["id"] ?: "",
                    vehicle_type_id = vehicleTypeState.value["id"] ?: "",
                    vehicle_model_id = vehicleModelState.value["id"] ?: "",
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
                        viewModel.updateFilterPolicyRates(result)
                        BottomBarScreen.Vehicle_Data.route?.let { mainNavController.navigate(it) }
                    }else{
                        Toast.makeText(context, "No data Found\n $result", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }


    }


}


@Composable
fun VehicleTypeSelection(
    vehicleTypes: List<Any>,
    selectedValue: MutableState<Map<String, String>>,
    selectionTitle: String,
    onItemSelected: (Any) -> Unit
) {
    val searchQuery = remember { mutableStateOf("") }
    val filteredVehicleTypes = remember(searchQuery.value, vehicleTypes) {
        vehicleTypes.filter { type ->
            getItemName(type).contains(searchQuery.value, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // LazyRow for Vehicle Types
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredVehicleTypes) { vehicleType ->
                val isSelected = selectedValue.value[selectionTitle] == getItemName(vehicleType)
//                val isSelected = true
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            selectedValue.value = mapOf(selectionTitle to getItemName(vehicleType))
                            onItemSelected(vehicleType)
                        }

                ) {
                    Card(
                        modifier = Modifier
                            .size(100.dp)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp, // Increase the selected border width
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface,
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize(1f)
                                .background(Color.White)
                        ) {
                            SubcomposeAsyncImage(
                                model = getItemImageUrl(vehicleType),
                                contentDescription = "Vehicle Type Image",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.fillMaxSize(),
                                loading = { CircularProgressIndicator() },
                                error = {
                                    Image(
                                        painter = painterResource(R.drawable.dummy_image),
                                        contentDescription = "Error Image",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = getItemName(vehicleType),
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(100.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

// Helper Functions
fun getItemName(item: Any): String {
    // Replace with the logic to extract the name from your data model
    return (item as? VehicleData)?.name ?: "Unknown"
}

fun getItemImageUrl(item: Any): String {
    // Replace with the logic to extract the image URL from your data model
    return (item as? VehicleData)?.media_url ?: ""
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleBrandSelection(
    vehicleBrand: List<Any>,
    selectedValue: MutableState<Map<String, String>>,
    selectionTitle: String,
    onItemSelected: (Any) -> Unit
) {
    val searchQuery = remember { mutableStateOf("") }
    val showBottomSheet = remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()

    // Filtered list based on the search query
    val filteredVehicleBrand = remember(searchQuery.value, vehicleBrand) {
        vehicleBrand.filter { type ->
            getItemBrandName(type).contains(searchQuery.value, ignoreCase = true)
        }.sortedBy { getItemBrandName(it) }
    }

    // Identify the selected brand's index
    val selectedBrandIndex = remember(selectedValue.value, vehicleBrand) {
        filteredVehicleBrand.indexOfFirst {
            getItemBrandName(it) == selectedValue.value[selectionTitle]
        }
    }

    // Ensure selected brand appears first in the list
    val displayedVehicleBrand = remember(filteredVehicleBrand, selectedBrandIndex) {
        if (selectedBrandIndex in filteredVehicleBrand.indices) {
            listOf(filteredVehicleBrand[selectedBrandIndex]) +
                    filteredVehicleBrand.filterIndexed { index, _ -> index != selectedBrandIndex }
        } else {
            filteredVehicleBrand
        }.take(7)
    }

    // Auto-scroll to the selected item when it changes
    LaunchedEffect(selectedValue.value) {
        val firstIndex = displayedVehicleBrand.indexOfFirst {
            getItemBrandName(it) == selectedValue.value[selectionTitle]
        }
        if (firstIndex >= 0) {
            lazyListState.animateScrollToItem(firstIndex)
        }
    }

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false },
            containerColor = MaterialTheme.colorScheme.background,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) { // Search TextField
                androidx.compose.material.TextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    placeholder = {
                        Text(
                            text = "Search...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredVehicleBrand) { vehicleBrand ->
                    val isSelected =
                        selectedValue.value[selectionTitle] == getItemBrandName(vehicleBrand)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedValue.value =
                                    mapOf(
                                        selectionTitle to getItemBrandName(vehicleBrand),
                                        "id" to getItemBrandId(vehicleBrand)
                                    )
                                onItemSelected(vehicleBrand)
                                showBottomSheet.value = false
                            }
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        SubcomposeAsyncImage(
                            model = getItemImageBrandUrl(vehicleBrand),
                            contentDescription = "Vehicle Brand Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                ),
                            loading = { CircularProgressIndicator() },
                            error = {
                                Image(
                                    painter = painterResource(R.drawable.dummy_image),
                                    contentDescription = "Error Image",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            modifier = Modifier.width(48.dp),
                            textAlign = TextAlign.Center,
                            text = getItemBrandName(vehicleBrand),
                            maxLines = 1,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        LazyRow(
            state = lazyListState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(displayedVehicleBrand) { vehicleBrand ->
                val isSelected =
                    selectedValue.value[selectionTitle] == getItemBrandName(vehicleBrand)

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            selectedValue.value =
                                mapOf(selectionTitle to getItemBrandName(vehicleBrand), "id" to getItemBrandId(vehicleBrand))
                            onItemSelected(vehicleBrand)
                        }
                ) {
                    Card(
                        modifier = Modifier
                            .size(79.dp, 74.dp)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            SubcomposeAsyncImage(
                                model = getItemImageBrandUrl(vehicleBrand),
                                contentDescription = "Vehicle Brand Image",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier,
                                loading = { CircularProgressIndicator() },
                                error = {
                                    Image(
                                        painter = painterResource(R.drawable.dummy_image),
                                        contentDescription = "Error Image",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = getItemBrandName(vehicleBrand),
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(100.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            if (filteredVehicleBrand.size > 7) {
                item {
                    Card(
                        modifier = Modifier
                            .size(79.dp, 74.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = "View All",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable {
                                    showBottomSheet.value = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}



// Helper Functions
fun getItemBrandName(item: Any): String {
    // Replace with the logic to extract the name from your data model
    return (item as? BrandData)?.name.toString()
}

fun getItemBrandId(item: Any): String {
    // Replace with the logic to extract the name from your data model
    return (item as? BrandData)?.id.toString()
}

fun getItemImageBrandUrl(item: Any): String {
    // Replace with the logic to extract the image URL from your data model
    return (item as? BrandData)?.media_url ?: ""
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleCitySelection(
    city: List<Any>,
    selectedValue: MutableState<Map<String, String>>,
    selectionTitle: String,
    onItemSelected: (Any) -> Unit
) {
    val searchQuery = remember { mutableStateOf("") }
    val showBottomSheet = remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()

    // Filtered list based on the search query
//    val filteredCityList = remember(searchQuery.value, city) {
//        city.filter { type ->
//            getItemCityName(type).contains(searchQuery.value, ignoreCase = true)
//        }
//    }

    val filteredCityList = remember(searchQuery.value, city) {
        city.filter { type ->
            getItemCityName(type).contains(searchQuery.value, ignoreCase = true)
        }.sortedBy { getItemCityName(it) }
    }
    // Identify the selected city's index
    val selectedCityIndex = remember(selectedValue.value, city) {
        filteredCityList.indexOfFirst { getItemCityName(it) == selectedValue.value[selectionTitle] }
    }

    // Ensure selected city appears first in the list
    val displayedCityList = remember(filteredCityList, selectedCityIndex) {
        if (selectedCityIndex in filteredCityList.indices) {
            listOf(filteredCityList[selectedCityIndex]) +
                    filteredCityList.filterIndexed { index, _ -> index != selectedCityIndex }
        } else {
            filteredCityList
        }.take(7)
    }

    LaunchedEffect(selectedValue.value) {
        val firstIndex = displayedCityList.indexOfFirst {
            getItemCityName(it) == selectedValue.value[selectionTitle]
        }
        if (firstIndex >= 0) {
            lazyListState.animateScrollToItem(firstIndex)
        }
    }

    if (showBottomSheet.value) {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { showBottomSheet.value = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            // Search bar and grid layout for bottom sheet content
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material.TextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    placeholder = {
                        Text(
                            text = "Search...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                items(filteredCityList) { city ->
                    val isSelected = getItemCityName(city) == selectedValue.value[selectionTitle]

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedValue.value =
                                    mapOf(selectionTitle to getItemCityName(city))
                                onItemSelected(city)
                                showBottomSheet.value = false // Close the bottom sheet
                            }
                            .padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        SubcomposeAsyncImage(
                            model = getItemCityImageUrl(city),
                            contentDescription = "City Image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.size(48.dp),
                            loading = { CircularProgressIndicator() },
                            error = {
                                val initials = getItemCityName(city).split(" ")
                                    .joinToString("") { it.take(2) }.uppercase()

                                if (initials != null) {
                                    Text(
                                        text = initials,
                                        color = Color.Black,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                            }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            modifier = Modifier.width(48.dp),
                            textAlign = TextAlign.Center,
                            text = getItemCityName(city),
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        LazyRow(
            state = lazyListState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(displayedCityList) { city ->
                val isSelected = getItemCityName(city) ==
                        selectedValue.value[selectionTitle]

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        selectedValue.value =
                            mapOf(selectionTitle to getItemCityName(city))
                        onItemSelected(city)
                    }
                ) {
                    Card(
                        modifier = Modifier
                            .size(79.dp, 74.dp)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface,
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            SubcomposeAsyncImage(
                                model = getItemCityImageUrl(city),
                                contentDescription = "City Image",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier,
                                loading = { CircularProgressIndicator() },
                                error = {
                                    val initials = getItemCityName(city).split(" ").joinToString(" ") { it.take(2) }.uppercase()
                                    if (initials != null) {
                                        Text(
                                            text = initials,
                                            color = Color.Black,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = getItemCityName(city),
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(100.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            if (filteredCityList.size > 7) {
                item {
                    Card(
                        modifier = Modifier
                            .size(79.dp, 74.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = "View All",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable {
                                    showBottomSheet.value = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Helper Functions
fun getItemCityName(item: Any): String {
    return (item as? CityData)?.name.toString()
}

fun getItemCityImageUrl(item: Any): String {
    return (item as? CityData)?.media_url ?: ""
}
