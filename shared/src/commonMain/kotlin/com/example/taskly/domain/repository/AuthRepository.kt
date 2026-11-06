package com.example.taskly.domain.repository

import com.example.taskly.domain.common.ApiResult
import com.example.taskly.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): ApiResult<Unit>
    suspend fun refresh(): ApiResult<Unit>
    suspend fun getCurrentUser(): ApiResult<User>
    suspend fun isLoggedIn(): Boolean
    suspend fun logout()
}