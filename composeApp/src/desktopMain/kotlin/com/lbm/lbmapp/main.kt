package com.lbm.lbmapp

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import java.io.InputStream

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    // Carrega o ícone como ImageBitmap
    val iconStream: InputStream? = Thread.currentThread()
        .contextClassLoader
        .getResourceAsStream("logo_lbm_background.jpg")

    val iconBitmap = iconStream?.use { it.readAllBytes().decodeToImageBitmap() }

    // Cria um Painter a partir do ImageBitmap
    val painter = iconBitmap?.let { BitmapPainter(it) }

    Window(
        onCloseRequest = ::exitApplication,
        title = "LBMApp",
        icon = painter!!
    ) {
        App()
    }
}