package com.example.taskly.di

import com.example.taskly.data.AppSettings
import com.example.taskly.data.auth.service.AppAuthApiService
import com.example.taskly.data.auth.service.AuthApiService
import com.example.taskly.data.network.createHttpClient
import com.example.taskly.data.repository.AuthRepositoryImpl
import com.example.taskly.data.repository.OnboardingRepositoryImpl
import com.example.taskly.data.security.AppSecureStorage
import com.example.taskly.data.security.SecureStorage
import com.example.taskly.domain.common.ApiResult
import com.example.taskly.domain.repository.AuthRepository
import com.example.taskly.domain.repository.OnboardingRepository
import com.example.taskly.domain.usecase.GetCurrentUserUseCase
import com.example.taskly.domain.usecase.IsLoggedInUseCase
import com.example.taskly.domain.usecase.IsOnboardingCompletedUseCase
import com.example.taskly.domain.usecase.LoginUseCase
import com.example.taskly.domain.usecase.LogoutUseCase
import com.example.taskly.domain.usecase.SetOnboardingCompletedUseCase
import com.example.taskly.domain.usecase.ValidatePasswordUseCase
import com.example.taskly.domain.usecase.ValidateUsernameUseCase
import com.example.taskly.ui.auth.AuthViewModel
import com.example.taskly.ui.home.HomeViewModel
import com.example.taskly.ui.onboarding.OnboardingViewModel
import com.russhwolf.settings.Settings
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val PLAIN_CLIENT = "plainClient"
private const val API_CLIENT = "apiClient"

val dataModule = module {
    single<Settings> { Settings() }
    singleOf(::AppSettings)
    singleOf(::OnboardingRepositoryImpl) { bind<OnboardingRepository>() }
}

val secureModule = module {
    includes(platformModule)
    singleOf(::AppSecureStorage) { bind<SecureStorage>() }
}

val networkModule = module {
    includes(secureModule)

    single(named(PLAIN_CLIENT)) { createHttpClient() }

    single(named(API_CLIENT)) {
        val secureStorage: SecureStorage = get()
        createHttpClient {
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(secureStorage.getAccessToken(), secureStorage.getRefreshToken())
                    }
                    refreshTokens {
                        // Спрацьовує, коли сервер відповів 401 (токен прострочений)
                        val authRepository: AuthRepository = get()
                        when (authRepository.refresh()) {
                            is ApiResult.Success -> BearerTokens(
                                secureStorage.getAccessToken(),
                                secureStorage.getRefreshToken(),
                            )
                            is ApiResult.Failure -> {
                                authRepository.logout()
                                null
                            }
                        }
                    }
                    sendWithoutRequest { true }
                }
            }
        }
    }

    single<AuthApiService> {
        AppAuthApiService(plainClient = get(named(PLAIN_CLIENT)), apiClient = get(named(API_CLIENT)))
    }
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
}

val appModule = module {
    includes(dataModule, networkModule)

    factoryOf(::IsOnboardingCompletedUseCase)
    factoryOf(::SetOnboardingCompletedUseCase)
    factoryOf(::ValidateUsernameUseCase)
    factoryOf(::ValidatePasswordUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::IsLoggedInUseCase)
    factoryOf(::GetCurrentUserUseCase)

    viewModelOf(::OnboardingViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AuthViewModel)
}