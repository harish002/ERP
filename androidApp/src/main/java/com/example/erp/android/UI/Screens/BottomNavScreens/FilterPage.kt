package com.example.erp.android.UI.Screens.BottomNavScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.erp.android.ERPTheme
import com.example.erp.android.UI.Screens.PolicyListView
import com.example.erp.android.UI.Screens.SelectionView
import com.example.lms.android.Services.Methods
import com.example.lms.android.ui.Component.Cust_Btn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen() {
    val context = LocalContext.current

    val sheetState = androidx.compose.material3.rememberModalBottomSheetState()

    var filterSheet by remember { mutableStateOf(false) }

    // Initialize states for vehicle type and fuel type
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

    // Data for various fields
    val vehicleTypes = listOf("Car", "Truck", "Motorcycle", "Bicycle")
    val fuelTypes = listOf("Petrol", "Diesel", "Electric", "Hybrid")
    val ncbTypes = listOf("YES", "NO")  // Static for NCB Type
    val states = listOf("MP", "UP", "HR", "DL")
    val cityCategories = listOf("Cat A", "Cat B", "Cat C")
    val cities = listOf("YES", "NO", "Asdf", "NO")
    val insuranceTypes = listOf("Comprehensive", "Third-Party", "Own Damage")
    val renewalTypes = listOf("Rollover", "Brand New", "New")
    val insurers = listOf("Tata", "Bajaj", "Reliance", "ICICI")


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

    var loading by remember { mutableStateOf(true) }

    ERPTheme {
        LaunchedEffect(context) {
            Methods().retrieve_Token(context)?.let {
                loading = false
            }

        }
//        val allCategorylist = viewModel.allCourseCate.collectAsState()
//        val allPublishlist = viewModel.allPublishesCourses.collectAsState()
        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onBackground)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Sales Tools")

                            TextButton(
                                modifier = Modifier,
                                onClick = {
                                    filterSheet = true
                                }) {
                                Text(

                                    text = "Filter",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }


                    },
                    actions = {
                        // Add the navigation icon here
                        Box(
                            modifier = Modifier
                                .wrapContentSize(),
                            // Adjust padding as needed
                            contentAlignment = Alignment.CenterEnd
                        ) {
//                            NavigationIcon(navController = mainNavController)
                        }
                    },
                    navigationIcon = {
                        if (scrollBehavior.state.collapsedFraction < 1f) {
                            CircularProfileWithWelcome("Test User")
                        }
//                        CircularProfileWithWelcome("Test User")
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        scrolledContainerColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = Color.Black,
                        titleContentColor = Color.Black,
                        actionIconContentColor = Color.LightGray,
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(MaterialTheme.colorScheme.onBackground)
            ) {

                items(7) {
                    PolicyListView()
                }

                item {
                    val selectedValue = remember { mutableStateOf(mapOf<String, String>()) }
                    val dropDownViewSelected = remember { mutableStateOf(mapOf<String, Boolean>()) }
                    val vehicleTypes = listOf("Car", "Truck", "Motorcycle", "Bicycle")

                    SelectionView(
                        selectionTitle = "Vehicle Type",
                        staticValue = "Please select a vehicle",
                        selectedValue = selectedValue,
                        dropDownViewSelected = dropDownViewSelected,
                        vehicleTypes = vehicleTypes
                    )
                }

            }
            if (filterSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        filterSheet = false
                    },
                    sheetState = sheetState
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 4.dp)
                    ) {
                        item {
                            Row(
                                Modifier.fillMaxWidth(1f),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Cust_Btn(text = "Reset", isSplit = true) {
                                }
                                Spacer(modifier = Modifier.padding(2.dp))
                                Cust_Btn(text = "Apply", isblue = true) {
                                }
                            }
                        }
                        item {
                            Text(
                                text = "Vehicle Details",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 16.dp)
                            )

                            // Vehicle Type SelectionView
                            SelectionView(
                                selectionTitle = "Vehicle Type",
                                staticValue = "Please select a vehicle",
                                selectedValue = vehicleTypeState,
                                dropDownViewSelected = vehicleTypeDropdownState,
                                vehicleTypes = vehicleTypes
                            )

                            // Fuel Type SelectionView
                            SelectionView(
                                selectionTitle = "Fuel Type",
                                staticValue = "Please select a fuel",
                                selectedValue = fuelTypeState,
                                dropDownViewSelected = fuelTypeDropdownState,
                                vehicleTypes = fuelTypes
                            )

                            // NCB Type SelectionView
                            SelectionView(
                                selectionTitle = "NCB Type",
                                staticValue = "Please select an NCB Type",
                                selectedValue = ncbState,
                                dropDownViewSelected = ncbDropdownState,
                                vehicleTypes = ncbTypes
                            )

                            Text(
                                text = "Loction Details",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 16.dp)
                            )
                            // State SelectionView
                            SelectionView(
                                selectionTitle = "State",
                                staticValue = "Please select a state",
                                selectedValue = stateState,
                                dropDownViewSelected = stateDropdownState,
                                vehicleTypes = states
                            )

                            // City Category SelectionView
                            SelectionView(
                                selectionTitle = "City Category",
                                staticValue = "Please select a city category",
                                selectedValue = cityCategoryState,
                                dropDownViewSelected = cityCategoryDropdownState,
                                vehicleTypes = cityCategories
                            )

                            // City SelectionView
                            SelectionView(
                                selectionTitle = "City",
                                staticValue = "Please select a city",
                                selectedValue = cityState,
                                dropDownViewSelected = cityDropdownState,
                                vehicleTypes = cities
                            )
                            Text(
                                text = "Policy Details",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 16.dp)
                            )
//                            Divider(modifier = Modifier.padding(), color = Color.Black, thickness = 1.dp)
                            // Insurance Type SelectionView
                            SelectionView(
                                selectionTitle = "Insurance Type",
                                staticValue = "Please select an insurance type",
                                selectedValue = insuranceTypeState,
                                dropDownViewSelected = insuranceTypeDropdownState,
                                vehicleTypes = insuranceTypes
                            )

                            // Renewal Type SelectionView
                            SelectionView(
                                selectionTitle = "Renewal Type",
                                staticValue = "Please select a renewal type",
                                selectedValue = renewalTypeState,
                                dropDownViewSelected = renewalTypeDropdownState,
                                vehicleTypes = renewalTypes
                            )

                            // Insurer SelectionView
                            SelectionView(
                                selectionTitle = "Insurer",
                                staticValue = "Please select an insurer",
                                selectedValue = insurerState,
                                dropDownViewSelected = insurerDropdownState,
                                vehicleTypes = insurers
                            )

                        }
                    }

                }
            }
        }
    }
}


@Composable
fun CircularProfileWithWelcome(userName: String) {
    val initials = userName.split(" ").joinToString("") { it.take(1) }.uppercase()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp) // Optional padding for the row
    ) {
        // Circular Profile Icon
        Box(
            modifier = Modifier
                .size(60.dp) // Size of the circular icon
                .background(Color.Gray, shape = CircleShape) // Circular shape with background color
                .border(2.dp, Color.White, shape = CircleShape) // Optional border
                .padding(8.dp), // Padding inside the circle
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


@Composable
fun createSelectionState(): Pair<MutableState<Map<String, String>>, MutableState<Map<String, Boolean>>> {
    val selectedValue = remember { mutableStateOf(mapOf<String, String>()) }
    val dropDownViewSelected = remember { mutableStateOf(mapOf<String, Boolean>()) }
    return Pair(selectedValue, dropDownViewSelected)
}