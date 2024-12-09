package com.chp.erp.android.ui.screens.bottomNavScreens.Profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chp.erp.android.ERPTheme

@Composable
fun ProfileTabbedView(
    tabItems: List<ProfileTabData>,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = Color.LightGray,
    selectedBorder: Color = MaterialTheme.colorScheme.primary,
    unselectedBorder: Color = Color.Transparent
) {
    if (tabItems.isEmpty()) {
        return // Exit early if there are no tabs to display
    }

    var selectedTab by remember { mutableStateOf(tabItems.first()) }

    // Safely compute the selected index
    val selectedTabIndex = tabItems.indexOf(selectedTab).takeIf { it >= 0 } ?: 0

    ERPTheme  {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // TabRow with tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(0.dp),
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    if (selectedTabIndex in tabPositions.indices) {
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .align(Alignment.Start),
                            color = selectedColor // Ensure the indicator uses the correct color
                        )
                    }
                },
                divider = {}
            ) {
                tabItems.forEachIndexed { index, tab ->
                    Tab(
                        text = {
                            Text(
                                text = tab.title,
                                color = if (selectedTabIndex == index) selectedColor else Color.Black,
                                overflow = TextOverflow.Visible
                            )
                        },
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTab = tab
                            tab.action()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
//                            .border(
//                                1.dp,
//                                if (selectedTabIndex == index) selectedBorder else unselectedBorder
//                            )
//                            .background(
//                                if (selectedTabIndex == index) selectedColor else unselectedColor
//                            )
                    )
                }
            }

            // Content for each tab
            Box(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                selectedTab.content()
            }
        }
    }
}



data class ProfileTabData(
    val title: String,
    val action: () -> Unit,
    val content: @Composable () -> Unit
)
