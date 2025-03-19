package com.lbm.lbmapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.lbm.lbmapp.viewmodels.LoginViewModel
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
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val isDesktop = Platform.isDesktop()
    val username by remember { viewModel::username }
    val password by remember { viewModel::password }
    val showPassword by remember { viewModel::showPassword }
    val isLoading by remember { viewModel::isLoading }
    val errorMessage by remember { viewModel::errorMessage }
    var isDarkTheme by remember { mutableStateOf(false) }
    val (usernameFocus, passwordFocus) = remember { FocusRequester.createRefs() }

    // Função para criar o modificador comum dos campos de texto
    fun commonTextFieldModifier(
        isDesktop: Boolean,
        focusRequester: FocusRequester,
        onKeyEvent: (keyEvent: androidx.compose.ui.input.key.KeyEvent) -> Boolean
    ): Modifier {
        return Modifier
            .fillMaxWidth(if (isDesktop) 0.4f else 1f)
            .focusRequester(focusRequester)
            .onKeyEvent(onKeyEvent)
    }

    // Componente reutilizável para campos de texto
    @Composable
    fun CustomTextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        isPassword: Boolean = false,
        showPassword: Boolean = false,
        focusRequester: FocusRequester,
        imeAction: ImeAction,
        keyboardActions: KeyboardActions,
        trailingIcon: @Composable() (() -> Unit)? = null,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue.filter { it !in listOf('\t', '\n') })
            },
            label = { Text(label, color = MaterialTheme.colors.onSurface) },
            visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = trailingIcon,
            modifier = commonTextFieldModifier(isDesktop, focusRequester) { keyEvent ->
                when (keyEvent.key) {
                    Key.Tab -> {
                        if (isPassword) usernameFocus.requestFocus() else passwordFocus.requestFocus()
                        true
                    }

                    Key.Enter -> {
                        viewModel.attemptLogin(onLoginSuccess)
                        true
                    }

                    else -> false
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = imeAction),
            keyboardActions = keyboardActions,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.primary,
                focusedBorderColor = MaterialTheme.colors.primary
            )
        )
    }

    LbmTheme(darkTheme = isDarkTheme) {
        Surface(color = MaterialTheme.colors.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Ícone de alternar tema
                IconButton(
                    onClick = { isDarkTheme = !isDarkTheme },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .pointerHoverIcon(PointerIcon.Hand)
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) FontAwesomeIcons.Solid.Moon else FontAwesomeIcons.Solid.Sun,
                        contentDescription = if (isDarkTheme) "Tema escuro" else "Tema claro",
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(Res.drawable.logo_lbm),
                        contentDescription = "Logo",
                        modifier = Modifier.size(200.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo de Usuário
                    CustomTextField(
                        value = username,
                        onValueChange = { viewModel.updateUsername(it) },
                        label = "Usuário",
                        focusRequester = usernameFocus,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { passwordFocus.requestFocus() }
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de Senha
                    CustomTextField(
                        value = password,
                        onValueChange = { viewModel.updatePassword(it) },
                        label = "Senha",
                        isPassword = true,
                        showPassword = showPassword,
                        focusRequester = passwordFocus,
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(
                            onDone = { viewModel.attemptLogin(onLoginSuccess) }
                        ),
                        trailingIcon = {
                            IconButton(
                                onClick = { viewModel.toggleShowPassword() },
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                            ) {
                                Icon(
                                    imageVector = if (showPassword) FontAwesomeIcons.Solid.EyeSlash else FontAwesomeIcons.Solid.Eye,
                                    contentDescription = if (showPassword) "Ocultar senha" else "Mostrar senha",
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colors.onSurface
                                )
                            }
                        }
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
                        onClick = { viewModel.attemptLogin(onLoginSuccess) },
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
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .pointerHoverIcon(PointerIcon.Hand),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}