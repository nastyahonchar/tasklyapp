package com.example.taskly.domain.usecase

import com.example.taskly.domain.common.ApiResult
import com.example.taskly.domain.model.User
import com.example.taskly.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): ApiResult<Unit> =
        repository.login(username, password)
}

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}

class IsLoggedInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Boolean = repository.isLoggedIn()
}

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): ApiResult<User> = repository.getCurrentUser()
}