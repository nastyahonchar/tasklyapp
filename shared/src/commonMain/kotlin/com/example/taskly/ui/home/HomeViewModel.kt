package com.example.taskly.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskly.domain.usecase.IsOnboardingCompletedUseCase
import com.example.taskly.domain.usecase.SetOnboardingCompletedUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(val isChecking: Boolean = true)

sealed interface HomeIntent {
    data object CheckOnboarding : HomeIntent
    data object ShowOnboardingAgain : HomeIntent
}

sealed interface HomeEffect {
    data object NavigateToOnboarding : HomeEffect
}

class HomeViewModel(
    private val isOnboardingCompleted: IsOnboardingCompletedUseCase,
    private val setOnboardingCompleted: SetOnboardingCompletedUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effects = Channel<HomeEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.CheckOnboarding -> viewModelScope.launch {
                _state.update { it.copy(isChecking = true) }
                if (isOnboardingCompleted()) {
                    _state.update { it.copy(isChecking = false) }
                } else {
                    _effects.send(HomeEffect.NavigateToOnboarding)
                }
            }

            HomeIntent.ShowOnboardingAgain -> viewModelScope.launch {
                setOnboardingCompleted(false)
                _effects.send(HomeEffect.NavigateToOnboarding)
            }
        }
    }
}