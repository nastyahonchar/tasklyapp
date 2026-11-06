package com.example.taskly.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskly.domain.common.ApiError
import com.example.taskly.domain.common.ApiResult
import com.example.taskly.domain.usecase.GetCurrentUserUseCase
import com.example.taskly.domain.usecase.IsLoggedInUseCase
import com.example.taskly.domain.usecase.IsOnboardingCompletedUseCase
import com.example.taskly.domain.usecase.LogoutUseCase
import com.example.taskly.domain.usecase.SetOnboardingCompletedUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val isChecking: Boolean = true,
    val userName: String = "",
)

sealed interface HomeIntent {
    data object CheckSession : HomeIntent
    data object ShowOnboardingAgain : HomeIntent
    data object SignOut : HomeIntent
}

sealed interface HomeEffect {
    data object NavigateToOnboarding : HomeEffect
    data object NavigateToAuth : HomeEffect
}

class HomeViewModel(
    private val isOnboardingCompleted: IsOnboardingCompletedUseCase,
    private val setOnboardingCompleted: SetOnboardingCompletedUseCase,
    private val isLoggedIn: IsLoggedInUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val logout: LogoutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effects = Channel<HomeEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.CheckSession -> checkSession()

            HomeIntent.ShowOnboardingAgain -> viewModelScope.launch {
                setOnboardingCompleted(false)
                _state.value = HomeState()
                _effects.send(HomeEffect.NavigateToOnboarding)
            }

            HomeIntent.SignOut -> viewModelScope.launch {
                logout()
                _state.value = HomeState()
                _effects.send(HomeEffect.NavigateToAuth)
            }
        }
    }

    private fun checkSession() {
        viewModelScope.launch {
            _state.update { it.copy(isChecking = true) }
            when {
                !isOnboardingCompleted() -> _effects.send(HomeEffect.NavigateToOnboarding)
                !isLoggedIn() -> _effects.send(HomeEffect.NavigateToAuth)
                else -> {
                    _state.update { it.copy(isChecking = false) }
                    loadUser()
                }
            }
        }
    }

    private suspend fun loadUser() {
        when (val result = getCurrentUser()) {
            is ApiResult.Success -> _state.update { it.copy(userName = result.data.firstName) }
            is ApiResult.Failure -> if (result.error == ApiError.InvalidCredentials) {
                logout()
                _state.value = HomeState()
                _effects.send(HomeEffect.NavigateToAuth)
            }
        }
    }
}