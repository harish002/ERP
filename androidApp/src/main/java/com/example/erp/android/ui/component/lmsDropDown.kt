package com.example.erp.android.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.erp.android.R

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
            Modifier.background(Color.Transparent)) {
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


//@Composable
//fun SelectionView(label: MutableState<String>):Boolean {
//    var isContentVisible by remember { mutableStateOf(false) }
//    var isEmailIdSelected by remember { mutableStateOf(true) }
//
//    Column(
//        modifier = Modifier
//            .padding(vertical = 12.dp)
//            .background(
//                color = Color.Transparent,
//                shape = RoundedCornerShape(8.dp)
//            )
//            .border(1.dp, Color(0xFF949494), RoundedCornerShape(8.dp))
//            .clickable { isContentVisible = !isContentVisible }
//            .padding(16.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Column(
//                modifier = Modifier.weight(1f),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Row(Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween){
//                    Column(
//                        Modifier.wrapContentWidth(),
//                        verticalArrangement = Arrangement.Center,
//                    ) {
//                        Text(
//                            text = "Choose Credential Type",
//                            fontSize = 14.sp,
//                            color = MaterialTheme.colorScheme.background,
//                            fontWeight = FontWeight.Normal
//                        )
//
//                        if(!isContentVisible){
//                            Text(
//                                text = if (isEmailIdSelected) "Email ID" else "Phone Number",
//                                fontSize = 16.sp,
//                                color = MaterialTheme.colorScheme.onSecondary,
//                                fontWeight = FontWeight.SemiBold,
//                                modifier = Modifier.padding(vertical = 4.dp)
//                            )
//                        }
//                    }
//
//                    Image(
//                        painter = painterResource(
//                            id = if (!isContentVisible) R.drawable.union_2
//                            else R.drawable.union_1
//                        ),
//                        contentDescription = null
//                    )
//                }
//
//            }
//
//        }
//        if(isContentVisible){
//            Spacer(modifier = Modifier.padding(10.dp))}
//        if (isContentVisible) {
//            Credential_Option(
//                text = "Email ID",
//                isSelected = isEmailIdSelected,
//                onClick = {
//                    isEmailIdSelected = true
//                    label.value = "Enter Email ID"
//                    isContentVisible = false
//                }
//            )
//
//            Credential_Option(
//                text = "Phone Number",
//                isSelected = !isEmailIdSelected,
//                onClick = {
//                    isEmailIdSelected = false
//                    label.value = "Enter Phone Number"
//                    isContentVisible = false
//                }
//            )
//        }
//    }
//    return isEmailIdSelected
//}
//
//@Composable
//fun Credential_Option(text: String, isSelected: Boolean, onClick: () -> Unit) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 14.dp)
//            .clickable(onClick = onClick),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = text,
//            fontSize = 16.sp,
//            color = MaterialTheme.colorScheme.onSecondary,
//            fontWeight = FontWeight.SemiBold,
//            modifier = Modifier.padding(vertical = 4.dp).weight(1f),
//        )
//
//        Spacer(modifier = Modifier.width(8.dp))
//
//        val circleColor = if (isSelected) MaterialTheme.colorScheme.background
//        else Color.Transparent
//        val borderColor = if (isSelected) MaterialTheme.colorScheme.background
//        else MaterialTheme.colorScheme.background
//
//        Box(
//            modifier = Modifier
//                .size(14.dp)
//                .drawWithContent {
//                    drawCircle(
//                        color = circleColor,
//                        radius = size.width / 2 - 4.dp.toPx()
//                    )
//
//                    drawCircle(
//                        color = borderColor,
//                        radius = size.width / 2,
//                        style = Stroke(width = 2.dp.toPx())
//                    )
//                }
//        )
//
//    }
//}
//
//




@Composable
fun SelectionView(label: MutableState<String>, isforgot: String): Boolean {
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
                Text(
                    text = "Choose Credential Type",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Normal
                )

                if (!isContentVisible) {
                    Text(
                        text = if (isEmailIdSelected) "Email ID" else "Phone Number",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Image(
                painter = painterResource(
                    id = if (!isContentVisible) R.drawable.union_2 else R.drawable.union_1
                ),
                contentDescription = null
            )
        }

        if (isContentVisible) {
            Spacer(modifier = Modifier.padding(10.dp))

            if(isforgot == "forgot"){
                Credential_Option(
                    text = "Email ID",
                    isSelected = isEmailIdSelected,
                    onClick = {
                        isEmailIdSelected = true
                        label.value = "Enter Email ID"
                        isContentVisible = false
                    }
                )
            }else{
                Credential_Option(
                    text = "Email ID",
                    isSelected = isEmailIdSelected,
                    onClick = {
                        isEmailIdSelected = true
                        label.value = "Enter Email ID"
                        isContentVisible = false
                    }
                )



                Credential_Option(
                    text = "Phone Number",
                    isSelected = !isEmailIdSelected,
                    onClick = {
                        isEmailIdSelected = false
                        label.value = "Enter Phone Number"
                        isContentVisible = false
                    }
                )
            }
        }
    }
    return isEmailIdSelected
}

@Composable
fun Credential_Option(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(14.dp)
                .drawWithContent {
                    drawCircle(
                        color = if (isSelected)
                            Color.Blue else Color.Transparent,
                        radius = size.width / 2 - 4.dp.toPx()
                    )

                    drawCircle(
                        color = Color.Blue,
                        radius = size.width / 2,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
        )
    }
}
