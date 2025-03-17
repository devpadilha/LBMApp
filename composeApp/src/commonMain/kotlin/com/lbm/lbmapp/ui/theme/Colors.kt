package com.lbm.lbmapp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors

val Orange = Color(0xFFFD6400)
val Black = Color(0xFF000000)
val Gray = Color(0xFFC7C7C7)
val White = Color(0xFFFFFFFF)
val LightGray = Color(0xFFE0E0E0)
val DarkGray = Color(0xFF4A4A4A)
val SuccessColor = Color(0xFF4CAF50)
val WarningColor = Color(0xFFFFC107)
val InfoColor = Color(0xFF2196F3)

val LbmLightColors = lightColors(
    primary = Orange,
    primaryVariant = Orange.copy(alpha = 0.8f),
    secondary = Gray,
    secondaryVariant = DarkGray,
    background = White,
    surface = White,
    onPrimary = White,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,
    onError = White
)

val LbmDarkColors = darkColors(
    primary = Orange,
    primaryVariant = Orange.copy(alpha = 0.8f),
    secondary = Gray,
    secondaryVariant = LightGray,
    background = Black,
    surface = DarkGray,
    onPrimary = Black,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    onError = Black
)
