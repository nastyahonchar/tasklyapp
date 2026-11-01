package com.example.taskly.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.taskly.domain.usecase.SetOnboardingCompletedUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingState(val isSaving: Boolean = false)

sealed interface OnboardingIntent {
    data object Finish : OnboardingIntent
}

sealed interface OnboardingEffect {
    data object NavigateToAuth : OnboardingEffect
}

private val log = Logger.withTag("OnboardingVM")

class OnboardingViewModel(
    private val setOnboardingCompleted: SetOnboardingCompletedUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _effects = Channel<OnboardingEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            OnboardingIntent.Finish -> finish()
        }
    }

    private fun finish() {
        if (_state.value.isSaving) return
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            setOnboardingCompleted(true)
            log.i { "Onboarding result saved" }
            _effects.send(OnboardingEffect.NavigateToAuth)
            _state.update { it.copy(isSaving = false) }
        }
    }
}