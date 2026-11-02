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
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dataModule = module {
    single<Settings> { Settings() }
    singleOf(::AppSettings)
    singleOf(::OnboardingRepositoryImpl) { bind<OnboardingRepository>() }
}

val appModule = module {
    includes(dataModule)
    factoryOf(::IsOnboardingCompletedUseCase)
    factoryOf(::SetOnboardingCompletedUseCase)
    factoryOf(::ValidateUsernameUseCase)
    factoryOf(::ValidatePasswordUseCase)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AuthViewModel)
}