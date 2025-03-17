package com.lbm.lbmapp.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.lbm.lbmapp.controllers.AuthController
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Eye
import compose.icons.fontawesomeicons.solid.EyeSlash
import com.lbm.lbmapp.utils.Platform

@Composable
fun LoginScreen(
    authController: AuthController,
    onLoginSuccess: () -> Unit
) {
    val isDesktop = Platform.isDesktop() // Padrão singleton para aferir se é desktop ou não
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val (usernameField, passwordField) = remember { FocusRequester.createRefs() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campo de usuário
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuário") },
                modifier = Modifier
                    .fillMaxWidth(if(isDesktop) 0.4f else 1f),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(Modifier.height(16.dp))

            // Campo de senha
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { showPassword = !showPassword }
                    ) {
                        Icon(
                            imageVector = if (showPassword) FontAwesomeIcons.Solid.EyeSlash else FontAwesomeIcons.Solid.Eye,
                            contentDescription = if (showPassword) "Ocultar senha" else "Mostrar senha",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(if(isDesktop) 0.4f else 1f),
            )

            // Mensagem de erro
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Botão de login
            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (username.isBlank() || password.isBlank()) {
                        errorMessage = "Preencha todos os campos"
                    } else {
                        isLoading = true
                        authController.validateCredentials(username, password, {
                            isLoading = false
                            onLoginSuccess()
                        }, {
                            isLoading = false
                            errorMessage = "Erro ao tentar fazer login"
                        })
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth(if(isDesktop) 0.25f else 1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colors.onPrimary
                    )
                } else {
                    Text(
                        text = "Entrar",
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Texto "Esqueci a Senha"
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Red,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Esqueci a Senha")
                    }
                },
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Red,
                style = MaterialTheme.typography.body1
            )
        }
}
    // Validação em memória (SUBSTITUIR POSTERIORMENTE)
    fun validateCredentials(username: String, password: String): Boolean {
        val adminUser = "admin"
        val adminPassword = "admin1234"

        return username == adminUser && password == adminPassword
    TODO("Implementar banco de dados")
    }