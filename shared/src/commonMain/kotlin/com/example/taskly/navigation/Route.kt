package com.example.taskly.navigation

sealed interface Route {
    data object Home : Route
    data object Onboarding : Route
    data object Auth : Route
}