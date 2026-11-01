package com.example.taskly.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingRoute(
    onNavigateToAuth: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                OnboardingEffect.NavigateToAuth -> onNavigateToAuth()
            }
        }
    }
    OnboardingScreen(onFinish = { viewModel.onIntent(OnboardingIntent.Finish) })
}