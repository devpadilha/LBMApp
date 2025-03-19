package com.lbm.lbmapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lbm.lbmapp.controllers.AuthController

class LoginViewModel(private val authController: AuthController) {

    // Estado da UI
    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var showPassword by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Métodos para atualizar o estado
    fun updateUsername(newUsername: String) {
        username = newUsername
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun toggleShowPassword() {
        showPassword = !showPassword
    }

    // Metodo para tentar login
    fun attemptLogin(onLoginSuccess: () -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            errorMessage = "Preencha todos os campos"
            return
        }

        isLoading = true
        errorMessage = null

        authController.validateCredentials(
            username, password,
            onSuccess = {
                isLoading = false
                onLoginSuccess()
            },
            onFailure = {
                isLoading = false
                errorMessage = "Erro ao tentar fazer login"
            },
        )
    }
}