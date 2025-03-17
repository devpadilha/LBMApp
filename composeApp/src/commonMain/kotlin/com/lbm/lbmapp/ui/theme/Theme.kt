package com.lbm.lbmapp.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun LbmTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        LbmDarkColors
    } else {
        LbmLightColors
    }

    MaterialTheme(
        colors = colors,
        typography = LbmTypography,
        content = content
    )
}