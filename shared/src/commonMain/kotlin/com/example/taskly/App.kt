package com.example.taskly

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import co.touchlab.kermit.Logger
import com.example.taskly.data.AppSettings
import com.example.taskly.ui.home.HomeScreen
import com.example.taskly.ui.onboarding.OnboardingScreen
import com.example.taskly.ui.theme.TasklyTheme

private const val ALWAYS_SHOW_ONBOARDING = true

@Composable
fun App() {
    TasklyTheme {
        val appSettings = remember { AppSettings() }
        var onboardingDone by remember {
            mutableStateOf(!ALWAYS_SHOW_ONBOARDING && appSettings.onboardingCompleted)
        }

        if (!onboardingDone) {
            OnboardingScreen(onFinish = {
                appSettings.onboardingCompleted = true
                onboardingDone = true
                Logger.i("App") { "Onboarding completed" }
            })
        } else {
            HomeScreen(onResetOnboarding = {
                appSettings.onboardingCompleted = false
                onboardingDone = false
                Logger.i("App") { "Onboarding reset" }
            })
        }
    }
}