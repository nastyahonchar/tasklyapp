package com.example.taskly.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taskly.domain.usecase.ValidationError
import com.example.taskly.ui.common.CenteredScrollColumn
import com.example.taskly.ui.common.isTwoPaneLayout
import org.jetbrains.compose.resources.stringResource
import taskly.shared.generated.resources.*

private val FormMaxWidth = 440.dp
private const val BrandPaneWeight = 0.45f

@Composable
fun AuthScreen(state: AuthState, snackbarHostState: SnackbarHostState, onIntent: (AuthIntent) -> Unit) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        if (isTwoPaneLayout()) {
            AuthTwoPane(state, onIntent)
        } else {
            AuthSinglePane(state, onIntent, Modifier.padding(padding))
        }
    }
}

@Composable
private fun AuthSinglePane(state: AuthState, onIntent: (AuthIntent) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AuthHeader()
        Spacer(Modifier.height(32.dp))
        AuthForm(state, onIntent)
    }
}

@Composable
private fun AuthTwoPane(state: AuthState, onIntent: (AuthIntent) -> Unit) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(BrandPaneWeight)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .windowInsetsPadding(
                    WindowInsets.safeContent.only(WindowInsetsSides.Vertical)
                )
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AuthHeader(
                iconBackground = MaterialTheme.colorScheme.surface,
                titleColor = MaterialTheme.colorScheme.onSecondaryContainer,
                subtitleColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
        CenteredScrollColumn(
            spacing = 0.dp,
            modifier = Modifier
                .weight(1f - BrandPaneWeight)
                .fillMaxHeight()
                .windowInsetsPadding(
                    WindowInsets.safeContent.only(WindowInsetsSides.End + WindowInsetsSides.Vertical)
                ),
            contentPadding = PaddingValues(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AuthForm(state, onIntent, Modifier.widthIn(max = FormMaxWidth))
        }
    }
}

@Composable
private fun AuthHeader(
    iconBackground: Color = MaterialTheme.colorScheme.secondaryContainer,
    titleColor: Color = Color.Unspecified,
    subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.size(72.dp).clip(CircleShape).background(iconBackground),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp),
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            stringResource(Res.string.auth_title),
            style = MaterialTheme.typography.headlineMedium,
            color = titleColor,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(Res.string.auth_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = subtitleColor,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AuthForm(state: AuthState, onIntent: (AuthIntent) -> Unit, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = state.username,
            onValueChange = { onIntent(AuthIntent.UsernameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.username_label)) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            enabled = !state.isLoading,
            isError = state.usernameError != null,
            supportingText = { state.usernameError?.let { Text(usernameMessage(it)) } },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) }),
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onIntent(AuthIntent.PasswordChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            label = { Text(stringResource(Res.string.password_label)) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { onIntent(AuthIntent.TogglePasswordVisibility) }) {
                    Icon(
                        imageVector = if (state.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = stringResource(
                            if (state.isPasswordVisible) Res.string.hide_password else Res.string.show_password
                        ),
                    )
                }
            },
            singleLine = true,
            isError = state.passwordError != null,
            supportingText = { state.passwordError?.let { Text(passwordMessage(it)) } },
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboard?.hide()
                focusManager.clearFocus()
                onIntent(AuthIntent.SignIn)
            }),
        )
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                onIntent(AuthIntent.SignIn)
            },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(26.dp),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Text(stringResource(Res.string.sign_in), style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun usernameMessage(error: ValidationError): String = stringResource(
    when (error) {
        ValidationError.Empty -> Res.string.error_username_empty
        ValidationError.TooShort -> Res.string.error_username_short
        ValidationError.InvalidChars -> Res.string.error_username_chars
    }
)

@Composable
private fun passwordMessage(error: ValidationError): String = stringResource(
    when (error) {
        ValidationError.Empty -> Res.string.error_password_empty
        else -> Res.string.error_password_short
    }
)