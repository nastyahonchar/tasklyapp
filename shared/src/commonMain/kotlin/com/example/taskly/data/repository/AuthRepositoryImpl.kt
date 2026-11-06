package com.example.taskly.data.repository

import com.example.taskly.data.auth.service.AuthApiService
import com.example.taskly.data.security.SecureStorage
import com.example.taskly.domain.common.ApiResult
import com.example.taskly.domain.common.map
import com.example.taskly.domain.model.User
import com.example.taskly.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val secureStorage: SecureStorage,
    private val api: AuthApiService,
) : AuthRepository {

    override suspend fun login(username: String, password: String): ApiResult<Unit> =
        api.login(username, password).map {
            secureStorage.saveTokens(it.accessToken, it.refreshToken)
        }

    override suspend fun refresh(): ApiResult<Unit> =
        api.refresh(secureStorage.getRefreshToken()).map {
            secureStorage.saveTokens(it.accessToken, it.refreshToken)
        }

    override suspend fun getCurrentUser(): ApiResult<User> =
        api.getCurrentUser().map { User(username = it.username, firstName = it.firstName) }

    override suspend fun isLoggedIn(): Boolean = secureStorage.getAccessToken().isNotBlank()

    override suspend fun logout() = secureStorage.clearTokens()
}