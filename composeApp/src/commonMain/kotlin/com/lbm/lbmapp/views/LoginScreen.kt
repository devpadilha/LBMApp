package com.lbm.lbmapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
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
import com.lbm.lbmapp.generated.resources.Res
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Eye
import compose.icons.fontawesomeicons.solid.EyeSlash
import compose.icons.fontawesomeicons.solid.Moon
import compose.icons.fontawesomeicons.solid.Sun
import com.lbm.lbmapp.utils.Platform
import com.lbm.lbmapp.ui.theme.LbmTheme
import org.jetbrains.compose.resources.painterResource
import com.lbm.lbmapp.generated.resources.logo_lbm

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
    var isDarkTheme by remember { mutableStateOf(false) }
    val (usernameFocus, passwordFocus) = remember { FocusRequester.createRefs() }

    // Função para tentar efetuar o login
    fun attemptLogin() {
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
    }

    LbmTheme(darkTheme = isDarkTheme) {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            // Botão para alternar o tema
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    onClick = { isDarkTheme = !isDarkTheme },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .pointerHoverIcon(PointerIcon.Hand)
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) FontAwesomeIcons.Solid.Moon else FontAwesomeIcons.Solid.Sun,
                        contentDescription = if (isDarkTheme) "tema claro" else "tema escuro",
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo no topo da tela
                Image(
                    painter = painterResource(Res.drawable.logo_lbm),
                    contentDescription = "Logo",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Campo de usuário
                OutlinedTextField(
                    value = username,
                    onValueChange = { newValue ->
                        username = newValue.filter { it !in listOf('\t', '\n') }
                    },
                    label = { Text("Usuário", color = MaterialTheme.colors.onSurface) },
                    modifier = Modifier
                        .fillMaxWidth(if (isDesktop) 0.4f else 1f)
                        .focusRequester(usernameFocus)
                        .onKeyEvent { keyEvent ->
                            when (keyEvent.key) {
                                Key.Tab -> {
                                    passwordFocus.requestFocus()
                                    true
                                }

                                Key.Enter -> {
                                    attemptLogin()
                                    true
                                }

                                else -> false
                            }
                        },
                    // Configurações do teclado
                    keyboardOptions = KeyboardOptions(
                        // Define a ação do botão "Enter" no teclado como "Próximo"
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        // Define o comportamento quando o botão "Próximo" é pressionado
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onSurface,
                        cursorColor = MaterialTheme.colors.primary,
                        focusedBorderColor = MaterialTheme.colors.primary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de senha
                OutlinedTextField(
                    value = password,
                    onValueChange = { newValue ->
                        password = newValue.filter { it !in listOf('\t', '\n') }
                    },
                    label = { Text("Senha", color = MaterialTheme.colors.onSurface) },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { showPassword = !showPassword },
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Icon(
                                imageVector = if (showPassword) FontAwesomeIcons.Solid.EyeSlash else FontAwesomeIcons.Solid.Eye,
                                contentDescription = if (showPassword) "Ocultar senha" else "Mostrar senha",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colors.onSurface
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(if (isDesktop) 0.4f else 1f)
                        .focusRequester(passwordFocus)
                        .onKeyEvent { keyEvent ->
                            when (keyEvent.key) {
                                Key.Tab -> {
                                    usernameFocus.requestFocus()
                                    true
                                }

                                Key.Enter -> {
                                    attemptLogin()
                                    true
                                }

                                else -> false
                            }
                        },
                    // Configurações do teclado
                    keyboardOptions = KeyboardOptions(
                        // Define a ação do botão "Enter" no teclado como "Concluir"
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        // Define o comportamento quando o botão "Concluir" é pressionado
                        onDone = { focusManager.clearFocus() }
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onSurface,
                        cursorColor = MaterialTheme.colors.primary,
                        focusedBorderColor = MaterialTheme.colors.primary
                    )
                )

                // Mensagem de erro
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                        .fillMaxWidth(if (isDesktop) 0.25f else 1f)
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

                Spacer(modifier = Modifier.height(8.dp))

                // Texto "Esqueci a Senha"
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.error,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Esqueci a Senha")
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}