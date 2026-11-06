package com.example.taskly.ui.auth

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.taskly.domain.common.ApiError
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel
import taskly.shared.generated.resources.*

@Composable
fun AuthRoute(
    onSignedIn: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AuthEffect.NavigateToHome -> onSignedIn()
                is AuthEffect.ShowError -> {
                    val message = getString(effect.error.messageRes())
                    scope.launch { snackbarHostState.showSnackbar(message) }
                }
            }
        }
    }

    AuthScreen(state = state, snackbarHostState = snackbarHostState, onIntent = viewModel::onIntent)
}

private fun ApiError.messageRes(): StringResource = when (this) {
    ApiError.InvalidCredentials -> Res.string.error_invalid_credentials
    ApiError.Network -> Res.string.error_network
    ApiError.Server -> Res.string.error_server
    ApiError.Unknown -> Res.string.error_unknown
}