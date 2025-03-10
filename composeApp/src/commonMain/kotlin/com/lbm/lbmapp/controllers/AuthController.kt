package com.lbm.lbmapp.controllers
import com.lbm.lbmapp.models.Employer

class AuthController {
    private val adminUser = Employer("admin", "admin1234")

    fun validateCredentials(
        username: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        if (username == adminUser.username && password == adminUser.password) {
            onSuccess()
        } else {
            throw Exception()
        }
    }
}