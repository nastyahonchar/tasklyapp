package com.example.taskly.data.auth.service

import com.example.taskly.data.auth.model.request.LoginRequest
import com.example.taskly.data.auth.model.request.RefreshRequest
import com.example.taskly.data.auth.model.response.LoginResponse
import com.example.taskly.data.auth.model.response.RefreshResponse
import com.example.taskly.data.auth.model.response.UserResponse
import com.example.taskly.data.network.BASE_URL
import com.example.taskly.data.network.LOGIN_PATH
import com.example.taskly.data.network.ME_PATH
import com.example.taskly.data.network.REFRESH_PATH
import com.example.taskly.data.network.TOKEN_LIFETIME_MINS
import com.example.taskly.data.network.safeRequest
import com.example.taskly.domain.common.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AppAuthApiService(
    private val plainClient: HttpClient,
    private val apiClient: HttpClient,
) : AuthApiService {

    override suspend fun login(username: String, password: String): ApiResult<LoginResponse> =
        plainClient.safeRequest<LoginResponse> {
            post("$BASE_URL$LOGIN_PATH") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password, TOKEN_LIFETIME_MINS))
            }
        }

    override suspend fun refresh(refreshToken: String): ApiResult<RefreshResponse> =
        plainClient.safeRequest<RefreshResponse> {
            post("$BASE_URL$REFRESH_PATH") {
                contentType(ContentType.Application.Json)
                setBody(RefreshRequest(refreshToken, TOKEN_LIFETIME_MINS))
            }
        }

    override suspend fun getCurrentUser(): ApiResult<UserResponse> =
        apiClient.safeRequest<UserResponse> { get("$BASE_URL$ME_PATH") }
}