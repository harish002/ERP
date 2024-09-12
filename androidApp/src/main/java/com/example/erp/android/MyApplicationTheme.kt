package com.example.erp.android

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun ERPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {


    val colors = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFF2252CB),
            secondary = Color(0xFF03DAC5),
            tertiary = Color(0xFF3700B3),
            surface = Color(0xFFF8F8F8),
            background = Color(0xFF3960F6),//Some changes
//            background = Color(0xFFF8F8F8),
            onBackground = Color(0xFFF8F8F8),
            onPrimary = Color.DarkGray,
            error = Color(0xFFF63939),
            onSecondary = Color(0xFF4E4E4E)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF2252CB),
            secondary = Color(0xFF03DAC5),
            tertiary = Color(0xFF3700B3),
            surface = Color(0xFFF8F8F8),
            background = Color(0xFF3960F6),//Some changes
//            background = Color(0xFFF8F8F8),
            error = Color(0xFFF63939),
            onBackground = Color(0xFFF8F8F8),
            onSecondary = Color(0xFF4E4E4E),
            onPrimary = Color.White,
        )
    }
    val gilroy_black = FontFamily(Font(R.font.gilroy_black))
    val gilroy_blackitalic = FontFamily(Font(R.font.gilroy_blackitalic))
    val gilroy_bold = FontFamily(Font(R.font.gilroy_bold))
    val gilroy_bolditalic = FontFamily(Font(R.font.gilroy_bolditalic))
    val gilroy_extrabold = FontFamily(Font(R.font.gilroy_extrabold))
    val gilroy_extrabolditalic = FontFamily(Font(R.font.gilroy_extrabolditalic))
    val gilroy_heavy = FontFamily(Font(R.font.gilroy_heavy))
    val gilroy_heavyitalic = FontFamily(Font(R.font.gilroy_heavyitalic))
    val gilroy_light = FontFamily(Font(R.font.gilroy_light))
    val gilroy_lightitalic = FontFamily(Font(R.font.gilroy_lightitalic))
    val gilroy_medium = FontFamily(Font(R.font.gilroy_medium))
    val gilroy_mediumitalic = FontFamily(Font(R.font.gilroy_mediumitalic))
    val gilroy_regular = FontFamily(Font(R.font.gilroy_regular))
    val gilroy_regularitalic = FontFamily(Font(R.font.gilroy_regularitalic))
    val gilroy_semibold = FontFamily(Font(R.font.gilroy_semibold))
    val gilroy_semibolditalic = FontFamily(Font(R.font.gilroy_semibolditalic))
    val gilroy_thin = FontFamily(Font(R.font.gilroy_thin))
    val gilroy_thinitalic = FontFamily(Font(R.font.gilroy_thinitalic))
    val gilroy_ultralight = FontFamily(Font(R.font.gilroy_ultralight))
    val gilroy_ultralightitalic = FontFamily(Font(R.font.gilroy_ultralightitalic))

    val typography = Typography(

        labelSmall = TextStyle(
            fontFamily = gilroy_bold,
            fontSize = 14.sp
        ),
        labelMedium = TextStyle(
            fontFamily = gilroy_bold,
            fontSize = 24.sp
        ),
        labelLarge = TextStyle(
            fontFamily = gilroy_bold,
            fontSize = 36.sp
        ),

        bodyLarge = TextStyle(
            fontFamily = gilroy_semibold,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = gilroy_semibold,
            fontSize = 14.sp
        ),
        bodySmall = TextStyle(
            fontFamily = gilroy_semibold,
            fontSize = 12.sp
        ),
        titleLarge = TextStyle(
            fontFamily = gilroy_semibold,
            fontSize = 20.sp
        ),
        titleMedium = TextStyle(
            fontFamily = gilroy_semibold,
            fontSize = 18.sp
        ),
        titleSmall = TextStyle(
            fontFamily = gilroy_semibold,
            fontSize = 16.sp
        ),

        headlineLarge = TextStyle(
            fontFamily = gilroy_semibold,
            fontSize = 36.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = gilroy_semibold,
            fontSize = 34.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = gilroy_semibold,
            fontSize = 32.sp
        ),


        )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.statusBarColor = colors.onBackground.toArgb()
            window.navigationBarColor = colors.onSecondary.toArgb()
            val windowInsetsController = WindowInsetsControllerCompat(window, view)

            // Check if status bar icons should be dark (light background)
            if (!darkTheme) {
                windowInsetsController.setAppearanceLightStatusBars(true)
            } else {
                windowInsetsController.setAppearanceLightStatusBars(false)
            }
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme


        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )


}