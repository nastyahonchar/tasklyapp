package com.example.taskly.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoute(
    onNavigateToOnboarding: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeIntent.CheckOnboarding)
        viewModel.effects.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToOnboarding -> onNavigateToOnboarding()
            }
        }
    }

    if (state.isChecking) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
    } else {
        HomeScreen(onResetOnboarding = { viewModel.onIntent(HomeIntent.ShowOnboardingAgain) })
    }
}