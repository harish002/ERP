package com.example.erp.android.UI.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.lms.android.ui.Component.CredentialOption

@Composable
fun PolicyListView() {
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
            text = "it1",
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Payout %",
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            Text(
                text = "it1",
                color = Color.Black,

                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 2.dp)
            )


        }
        Spacer(modifier = Modifier.padding(4.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Insurer",
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            Text(
                text = "it1",
                color = Color.Black,

                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 2.dp)
            )


        }
        Spacer(modifier = Modifier.padding(4.dp))
        Column(
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
                text = "it1",
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

            PolicyToolsGridView { showPopup = false }
        }
    }
}


@Composable
fun PolicyToolsGridView(onDismiss: () -> Unit) {
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
                GridItem("Payout %", "value")
            }
            item() {
                GridItem("Insurer", "value")
            }
            item() {
                GridItem("Insurance Type", "value")
            }
            item() {
                GridItem("Vehicle Type", "value")
            }
            item() {
                GridItem("Renewal Type", "value")
            }
            item() {
                GridItem("Fuel Type", "value")
            }
            item() {
                GridItem("Stare", "value")
            }
            item() {
                GridItem("City Category", "value")
            }
            item() {
                GridItem("City", "value")
            }
            item() {
                GridItem("NCB", "value")
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
            text = values,
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge
        )

    }
}


@Composable
fun Selection_View(
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
                CredentialOption(
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
