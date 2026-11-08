package com.example.taskly.data.auth.service

import com.example.taskly.data.auth.model.response.LoginResponse
import com.example.taskly.data.auth.model.response.RefreshResponse
import com.example.taskly.data.auth.model.response.UserResponse
import com.example.taskly.domain.common.ApiResult

interface AuthApiService {
    suspend fun login(username: String, password: String): ApiResult<LoginResponse>
    suspend fun refresh(refreshToken: String): ApiResult<RefreshResponse>
    suspend fun getCurrentUser(): ApiResult<UserResponse>
}