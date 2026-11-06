package com.example.taskly.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.taskly.domain.common.ApiError
import com.example.taskly.domain.common.ApiResult
import com.example.taskly.domain.usecase.LoginUseCase
import com.example.taskly.domain.usecase.ValidatePasswordUseCase
import com.example.taskly.domain.usecase.ValidateUsernameUseCase
import com.example.taskly.domain.usecase.ValidationError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthState(
    val username: String = "",
    val password: String = "",
    val usernameError: ValidationError? = null,
    val passwordError: ValidationError? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
)

sealed interface AuthIntent {
    data class UsernameChanged(val value: String) : AuthIntent
    data class PasswordChanged(val value: String) : AuthIntent
    data object TogglePasswordVisibility : AuthIntent
    data object SignIn : AuthIntent
}

sealed interface AuthEffect {
    data object NavigateToHome : AuthEffect
    data class ShowError(val error: ApiError) : AuthEffect
}

private val log = Logger.withTag("AuthVM")

class AuthViewModel(
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val login: LoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effects = Channel<AuthEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.UsernameChanged ->
                _state.update { it.copy(username = intent.value, usernameError = null) }

            is AuthIntent.PasswordChanged ->
                _state.update { it.copy(password = intent.value, passwordError = null) }

            AuthIntent.TogglePasswordVisibility ->
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

            AuthIntent.SignIn -> signIn()
        }
    }

    private fun signIn() {
        val current = _state.value
        if (current.isLoading) return

        val usernameError = validateUsername(current.username)
        val passwordError = validatePassword(current.password)
        _state.update { it.copy(usernameError = usernameError, passwordError = passwordError) }
        if (usernameError != null || passwordError != null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = login(current.username.trim(), current.password)) {
                is ApiResult.Success -> {
                    log.i { "Signed in" }
                    _effects.send(AuthEffect.NavigateToHome)
                    _state.value = AuthState()
                }
                is ApiResult.Failure -> {
                    log.w { "Sign in failed: ${result.error}" }
                    _state.update { it.copy(isLoading = false) }
                    _effects.send(AuthEffect.ShowError(result.error))
                }
            }
        }
    }
}