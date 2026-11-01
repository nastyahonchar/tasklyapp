package com.example.taskly.di

import com.example.taskly.data.AppSettings
import com.example.taskly.data.repository.OnboardingRepositoryImpl
import com.example.taskly.domain.repository.OnboardingRepository
import com.example.taskly.domain.usecase.IsOnboardingCompletedUseCase
import com.example.taskly.domain.usecase.SetOnboardingCompletedUseCase
import com.example.taskly.domain.usecase.ValidatePasswordUseCase
import com.example.taskly.domain.usecase.ValidateUsernameUseCase
import com.example.taskly.ui.auth.AuthViewModel
import com.example.taskly.ui.home.HomeViewModel
import com.example.taskly.ui.onboarding.OnboardingViewModel
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Settings> { Settings() }
    single { AppSettings(get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }

    factory { IsOnboardingCompletedUseCase(get()) }
    factory { SetOnboardingCompletedUseCase(get()) }
    factory { ValidateUsernameUseCase() }
    factory { ValidatePasswordUseCase() }

    viewModel { OnboardingViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { AuthViewModel(get(), get()) }
}