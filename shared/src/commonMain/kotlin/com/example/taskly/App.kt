package com.example.taskly

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.taskly.di.appModule
import com.example.taskly.navigation.Route
import com.example.taskly.ui.auth.AuthRoute
import com.example.taskly.ui.home.HomeRoute
import com.example.taskly.ui.onboarding.OnboardingRoute
import com.example.taskly.ui.theme.TasklyTheme
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = { modules(appModule) }) {
        TasklyTheme {
            val backStack = remember { mutableStateListOf<Route>(Route.Home) }

            fun replaceWith(route: Route) {
                backStack.clear()
                backStack.add(route)
            }

            NavDisplay(
                backStack = backStack,
                onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                entryProvider = entryProvider<Route> {
                    entry<Route.Home> {
                        HomeRoute(onNavigateToOnboarding = { replaceWith(Route.Onboarding) })
                    }
                    entry<Route.Onboarding> {
                        OnboardingRoute(onNavigateToAuth = { replaceWith(Route.Auth) })
                    }
                    entry<Route.Auth> {
                        AuthRoute(onSignedIn = { replaceWith(Route.Home) })
                    }
                },
            )
        }
    }
}