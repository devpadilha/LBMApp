package com.lbm.lbmapp

import androidx.compose.runtime.*
import com.lbm.lbmapp.controllers.AuthController
import com.lbm.lbmapp.ui.theme.LbmTheme
import com.lbm.lbmapp.viewmodels.LoginViewModel
import com.lbm.lbmapp.views.LoginScreen
import com.lbm.lbmapp.views.MainScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    LbmTheme {
        val authController = remember { AuthController() }
        var isLoggedIn by remember { mutableStateOf(false) }

        if (isLoggedIn) {
            MainScreen()
        } else {
            LoginScreen(
                viewModel = LoginViewModel(authController),
                onLoginSuccess = { isLoggedIn = true }
            )
        }
    }
}