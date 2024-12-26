package com.chp.erp.android.ui.screens.bottomNavScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun DetailsTabbedView(
    tabItems: List<DetailTabData>,
    selectedTab: DetailTabData,
    onTabSelected: (DetailTabData) -> Unit,
    selectedColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedColor: Color = MaterialTheme.colorScheme.scrim,
    selectedBorder: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedBorder: Color = MaterialTheme.colorScheme.scrim
) {
    if (tabItems.isEmpty()) return

    val selectedTabIndex = tabItems.indexOf(selectedTab).takeIf { it >= 0 } ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                if (selectedTabIndex in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = selectedColor
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
                            color = if (selectedTabIndex == index) Color.White else Color(0xFFC4C4C4),
                            overflow = TextOverflow.Visible,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1
                        )
                    },
                    selected = selectedTabIndex == index,
                    onClick = {
                        onTabSelected(tab)
                        tab.action()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (selectedTabIndex == index) selectedBorder else unselectedBorder
                        )
                        .background(
                            if (selectedTabIndex == index) selectedColor else unselectedColor
                        )
                        .padding(8.dp)
                )
            }
        }

        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            selectedTab.content({})
        }
    }
}





data class DetailTabData(
    val title: String,
    val action: () -> Unit,
    val content: @Composable (Any?) -> Unit
)