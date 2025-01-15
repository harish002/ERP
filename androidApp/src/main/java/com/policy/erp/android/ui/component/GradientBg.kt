package com.policy.erp.android.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GradientBackground() {
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFEBF1FF),
        ),
        start = Offset(100f, 0f),
        end = Offset(700f, 0f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {

    }
}