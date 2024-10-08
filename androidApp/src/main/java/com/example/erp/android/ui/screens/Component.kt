package com.example.erp.android.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.erp.android.R
import com.example.lms.Services.Dataclass.CityCategoryData
import com.example.lms.Services.Dataclass.CityData
import com.example.lms.Services.Dataclass.FuelTypeData
import com.example.lms.Services.Dataclass.InsuranceTypeData
import com.example.lms.Services.Dataclass.InsurerData
import com.example.lms.Services.Dataclass.PolicyRateData
import com.example.lms.Services.Dataclass.RenewalTypeData
import com.example.lms.Services.Dataclass.StatesData
import com.example.lms.Services.Dataclass.VehicleData
import com.example.erp.android.ui.component.Credential_Option

@Composable
fun PolicyListView(data: PolicyRateData, index: Int) {
    var showPopup by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clickable { showPopup = !showPopup }
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .drawBehind {
                    drawCircle(
                        color = Color.Blue.copy(alpha = 0.5f),
                        radius = 60f
                    )
                },
            text = "$index",
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Payout %",
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            if (data != null) {
                Text(
                    text = data.payouts,
                    color = Color.Black,

                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }


        }
        Spacer(modifier = Modifier.padding(4.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Insurer",
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            data.insurer.name.let {
                Text(
                    text = it,
                    color = Color.Black,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }


        }
        Spacer(modifier = Modifier.padding(4.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Insurannce type",
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            Text(
                text = "${data.insurance_type?.name}",
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 2.dp)
            )


        }

    }

    if (showPopup) {
        Popup(
            onDismissRequest = { showPopup = false }, // Close popup when clicked outside
            alignment = Alignment.Center,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {

            PolicyToolsGridView(data) { showPopup = false }
        }
    }
}


@Composable
fun PolicyToolsGridView(data: PolicyRateData, onDismiss: () -> Unit) {
    val items = (1..5).toList() // Example data set
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(0.9f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Policy Rate",
                modifier = Modifier
                    .padding(14.dp),
                style = TextStyle(
                    fontSize = 24.sp, // Increase the font size to 24sp (you can adjust the size as needed)
                    fontWeight = FontWeight.Bold // Add bold style if desired
                )
            )

            Image(
                painter = painterResource(id = R.drawable.close_btn),
                modifier = Modifier
                    .padding(14.dp)
                    .clickable {
                        onDismiss()
                    }, contentDescription = "Close Button"
            )
        }
        LazyVerticalGrid(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .padding(8.dp),
            columns = GridCells.Fixed(2), // 3 columns
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item() {
                GridItem("Payout %", data.payouts)
            }
            item() {
                GridItem("Insurer", data.insurer.name)
            }
            item() {
                GridItem("Insurance Type", data.insurance_type.name)
            }
            item() {
                GridItem("Vehicle Type", data.vehicle_model.vehicle_type.name)
            }
            item() {
                GridItem("Renewal Type", data.renewal_type.name)
            }
            item() {
                GridItem("Fuel Type", data.fuel_type.name)
            }
            item() {
                GridItem("State", data.city.state.name)
            }
            item() {
                GridItem("City Category", data.city.city_category.name)
            }
            item() {
                GridItem("City", data.city.name)
            }
            item() {
                GridItem("NCB", data.status.toString())
            }
        }
    }

}

@Composable
fun GridItem(key: String, values: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Text(
            text = key,
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = when (values) {
                "1" -> "Yes"
                "0" -> "No"
                else -> values
            },
            color = when (values) {
                "1" -> Color.Green
                "0" -> Color.Red
                else -> Color.Black
            },
            style = MaterialTheme.typography.bodyLarge
        )

    }
}


@Composable
fun SelectionView(
    selectedValue: String,
    options: List<String>,
    label: String,
    onValueChangedEvent: (String) -> Unit
): Boolean {
    var isContentVisible by remember { mutableStateOf(false) }
    var isEmailIdSelected by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(1.dp, Color(0xFF949494), RoundedCornerShape(8.dp))
            .clickable { isContentVisible = !isContentVisible }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        Modifier.wrapContentWidth(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = label,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.background,
                            fontWeight = FontWeight.Normal
                        )

                        if (!isContentVisible) {
                            Text(
                                text = selectedValue,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }

                    Image(
                        painter = painterResource(
                            id = if (!isContentVisible) R.drawable.union_2
                            else R.drawable.union_1
                        ),
                        contentDescription = null
                    )
                }

            }

        }
        if (isContentVisible) {
            Spacer(modifier = Modifier.padding(10.dp))
        }
        if (isContentVisible) {
            for (i in 1..5) {
                Credential_Option(
                    text = "Email ID $i",
                    isSelected = isEmailIdSelected,
                    onClick = {
                        isEmailIdSelected = true
                        isContentVisible = false
                    }
                )
            }
        }
    }
    return isEmailIdSelected
}



@Composable
fun SelectionView(
    selectionTitle: String,
    staticValue: String,
    isfilter: Boolean = false,
    selectedValue: MutableState<Map<String, String>>,
    dropDownViewSelected: MutableState<Map<String, Boolean>>,
    listTypes: List<Any>?// Keep as List<Any?>
) {
    val isSelected = selectedValue.value[selectionTitle].isNullOrEmpty()
    var selectedId = ""
    val isDropdownVisible = dropDownViewSelected.value[selectionTitle] ?: false


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(4.dp)
            .then(if (isfilter) Modifier.width(200.dp) else Modifier.fillMaxWidth())
            .wrapContentHeight()
            .width(200.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(width = 1.2.dp, color = Color(0xFF949494), shape = RoundedCornerShape(8.dp))
            .clickable {
                dropDownViewSelected.value = dropDownViewSelected.value
                    .toMutableMap()
                    .apply {
                        put(selectionTitle, !(dropDownViewSelected.value[selectionTitle] ?: false))
                    }
            }
            .padding(if(isfilter){ 12.dp }else{0.dp})
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = selectionTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
            )

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isSelected) staticValue else selectedValue.value[selectionTitle]
                            ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                    )
                }

                if (isDropdownVisible) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedValue.value = selectedValue.value
                                        .toMutableMap()
                                        .apply {
                                            put(selectionTitle, "")
                                        }
                                    dropDownViewSelected.value =
                                        dropDownViewSelected.value
                                            .toMutableMap()
                                            .apply {
                                                put(selectionTitle, false)
                                            }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Optional static value row or any other UI component can be added here
                        }

                        // Iterate over the listTypes and handle different types
                        listTypes?.forEach { item ->
                            when (item) {
                                is FuelTypeData -> {
                                    SelectionRow(
                                        title = item.name, // Display name for FuelType
                                        isSelected = selectedValue.value[selectionTitle] == item.name,
                                        onSelect = {
//                                            selectedValue.value = selectedValue.value.toMutableMap().apply {
//                                                put(selectionTitle, item.name)
//                                            }
                                            selectedValue.value = mapOf(
                                                "id" to item.id,
                                                "name" to item.name
                                            ).toMutableMap().apply {
                                                put(selectionTitle, item.name)
                                            }

                                            dropDownViewSelected.value =
                                                dropDownViewSelected.value.toMutableMap().apply {
                                                    put(selectionTitle, false)
                                                }
                                        }
                                    )

                                }

                                is VehicleData -> {
                                    SelectionRow(
                                        title = item.name, // Display name for VehicleType
                                        isSelected = selectedValue.value[selectionTitle] == item.name,
                                        onSelect = {
                                            selectedValue.value = mapOf(
                                                "id" to item.id,
                                                "name" to item.name
                                            ).toMutableMap().apply {
                                                put(selectionTitle, item.name)
                                            }
                                            dropDownViewSelected.value =
                                                dropDownViewSelected.value.toMutableMap().apply {
                                                    put(selectionTitle, false)
                                                }
                                        }
                                    )

                                }

                                is String -> {
                                    SelectionRow(
                                        title = item, // Display name for VehicleType
                                        isSelected = selectedValue.value[selectionTitle] == item,
                                        onSelect = {
                                            selectedValue.value =
                                                selectedValue.value.toMutableMap().apply {
                                                    put(selectionTitle, item)
                                                }
                                            dropDownViewSelected.value =
                                                dropDownViewSelected.value.toMutableMap().apply {
                                                    put(selectionTitle, false)
                                                }
                                        }
                                    )

                                }

                                is StatesData -> {

                                    SelectionRow(
                                        title = item.name,
                                        // Display name for State
                                        isSelected = selectedValue.value[selectionTitle]
                                                == item.name,
                                        onSelect = {
                                            selectedValue.value = mapOf(
                                                "id" to item.id,
                                                "name" to item.name
                                            ).toMutableMap().apply {
                                                put(selectionTitle, item.name)
                                            }
                                            dropDownViewSelected.value = dropDownViewSelected.value
                                                .toMutableMap().apply {
                                                    put(selectionTitle, false)
                                                }
                                        }
                                    )


                                }

                                is CityCategoryData -> {
                                    SelectionRow(
                                        title = item.name, // Display name for CityCategory
                                        isSelected = selectedValue.value[selectionTitle] == item.name,
                                        onSelect = {
                                            selectedValue.value = mapOf(
                                                "id" to item.id,
                                                "name" to item.name
                                            ).toMutableMap().apply {
                                                put(selectionTitle, item.name)
                                            }
                                            dropDownViewSelected.value =
                                                dropDownViewSelected.value.toMutableMap().apply {
                                                    put(selectionTitle, false)
                                                }
                                        }
                                    )

                                }

                                is CityData -> {
                                    SelectionRow(
                                        title = item.name, // Display name for InsuranceType
                                        isSelected = selectedValue.value[selectionTitle] == item.name,
                                        onSelect = {
                                            selectedValue.value = mapOf(
                                                "id" to item.id,
                                                "name" to item.name
                                            ).toMutableMap().apply {
                                                put(selectionTitle, item.name)
                                            }
                                            dropDownViewSelected.value =
                                                dropDownViewSelected.value.toMutableMap().apply {
                                                    put(selectionTitle, false)
                                                }
                                        }
                                    )

                                }

                                is InsuranceTypeData -> {
                                    SelectionRow(
                                        title = item.name, // Display name for InsuranceType
                                        isSelected = selectedValue.value[selectionTitle] == item.name,
                                        onSelect = {
                                            selectedValue.value = mapOf(
                                                "id" to item.id,
                                                "name" to item.name
                                            ).toMutableMap().apply {
                                                put(selectionTitle, item.name)
                                            }
                                            dropDownViewSelected.value =
                                                dropDownViewSelected.value.toMutableMap().apply {
                                                    put(item.id, false)
                                                }
                                        }
                                    )
                                }

                                is InsurerData -> {
                                    SelectionRow(
                                        title = item.name, // Display name for InsuranceType
                                        isSelected = selectedValue.value[selectionTitle] == item.name,
                                        onSelect = {
                                            selectedValue.value = mapOf(
                                                "id" to item.id,
                                                "name" to item.name
                                            ).toMutableMap().apply {
                                                put(selectionTitle, item.name)
                                            }
                                            dropDownViewSelected.value =
                                                dropDownViewSelected.value.toMutableMap().apply {
                                                    put(selectionTitle, false)
                                                }
                                        }
                                    )

                                }

                                is RenewalTypeData -> {
                                    SelectionRow(
                                        title = item.name, // Display name for InsuranceType
                                        isSelected = selectedValue.value[selectionTitle] == item.name,
                                        onSelect = {
                                            selectedValue.value = mapOf(
                                                "id" to item.id,
                                                "name" to item.name
                                            ).toMutableMap().apply {
                                                put(selectionTitle, item.name)
                                            }
                                            dropDownViewSelected.value =
                                                dropDownViewSelected.value.toMutableMap().apply {
                                                    put(selectionTitle, false)
                                                }
                                        }
                                    )

                                }

                                else -> {
                                    // Handle unexpected item types if needed
                                }
                            }
                        }
                    }
                }
            }
        }

        Image(
            painter = painterResource(
                id = if (isDropdownVisible)
                    R.drawable.union_1 else R.drawable.union_2
            ),
            contentDescription = null,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}

@Composable
fun SelectionRow(
    title: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        if (isSelected) {
            Box(
                modifier = Modifier.size(16.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(Color(0xFF3960F6))
                    drawCircle(Color(0xFF3960F6), radius = 8.dp.toPx())
                }
            }
        }
    }
}

//test test

//@Composable
//fun SelectionView(
//    selectionTitle: String,
//    staticValue: String,
//    selectedValue: MutableState<Map<String, String>>,
//    dropDownViewSelected: MutableState<Map<String, Boolean>>,
//    vehicleTypes: List<Any?>
//){
//    val isSelected = selectedValue.value[selectionTitle].isNullOrEmpty()
//    val isDropdownVisible = dropDownViewSelected.value[selectionTitle] ?: false
//
//    Row(verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween,
//        modifier = Modifier
//            .padding(vertical = 4.dp)
//            .background(
//                color = Color.Transparent,
//                shape = RoundedCornerShape(8.dp)
//            )
//            .border(width = 1.2.dp, color = Color(0xFF949494), shape = RoundedCornerShape(8.dp))
//            .clickable {
//                dropDownViewSelected.value = dropDownViewSelected.value
//                    .toMutableMap()
//                    .apply {
//                        put(
//                            selectionTitle,
//                            !(dropDownViewSelected.value[selectionTitle] ?: false)
//                        )
//                    }
//            }
//            .padding(16.dp),){
//        Column(
//            modifier = Modifier.weight(1f),
//            horizontalAlignment = Alignment.Start,
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Text(
//                text = selectionTitle,
//                style = MaterialTheme.typography.bodyMedium,
//                color = Color.Black,
//            )
//
//            Column(
//                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.spacedBy(0.dp)
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(
//                        text = if (isSelected) staticValue else selectedValue.value[selectionTitle]
//                            ?: "",
//                        style = MaterialTheme.typography.bodyLarge,
//                        fontWeight = FontWeight.Medium,
//                        color = Color.Black,
//                    )
//
//                }
//
//                if (isDropdownVisible) {
//                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable {
//                                    selectedValue.value = selectedValue.value
//                                        .toMutableMap()
//                                        .apply {
//                                            put(selectionTitle, "")
//                                        }
//                                    dropDownViewSelected.value =
//                                        dropDownViewSelected.value
//                                            .toMutableMap()
//                                            .apply {
//                                                put(selectionTitle, false)
//                                            }
//                                },
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
////                        Text(
////                            text = staticValue,
////                            style = MaterialTheme.typography.bodyLarge,
////                            fontSize = 14.sp
////                        )
////
////                        Spacer(modifier = Modifier.weight(1f))
//
//                        }
//
//                        vehicleTypes.forEach { type ->
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .clickable {
//                                        selectedValue.value =
//                                            selectedValue.value
//                                                .toMutableMap()
//                                                .apply {
//                                                    put(selectionTitle, type)
//                                                }
//                                        dropDownViewSelected.value =
//                                            dropDownViewSelected.value
//                                                .toMutableMap()
//                                                .apply {
//                                                    put(selectionTitle, false)
//                                                }
//                                    },
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Text(
//                                    text = type,
//                                    style = MaterialTheme.typography.bodyLarge,
//                                    fontSize = 14.sp,
//                                    color = Color.Black,
//                                    modifier = Modifier.weight(1f)
//                                )
//
//                                if (selectedValue.value[selectionTitle] == type) {
//                                    Box(
//                                        modifier = Modifier.size(16.dp)
//                                    ) {
//                                        Canvas(modifier = Modifier.fillMaxSize()) {
//                                            drawCircle(Color(0xFF3960F6))
//                                            drawCircle(Color(0xFF3960F6), radius = 8.dp.toPx())
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        Image(
//            painter = painterResource(id = if(isDropdownVisible) R.drawable.union_1 else R.drawable.union_2),
//            contentDescription = null,
//            modifier = Modifier.padding(top = 5.dp)
//        )
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustDropdown(
    selectedValue: String,
    options: List<String>,
    label: String,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedValue,
            onValueChange = {},

            label = { androidx.compose.material.Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier.background(Color.Transparent)
        ) {
            options.forEach { option: String ->
                DropdownMenuItem(
                    text = { androidx.compose.material.Text(text = option) },
                    onClick = {
                        expanded = false
                        onValueChangedEvent(option)
                    }
                )
            }
        }
    }
}
