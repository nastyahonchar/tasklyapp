package com.example.taskly.data.auth.service

import com.example.taskly.data.auth.model.LoginRequest
import com.example.taskly.data.auth.model.LoginResponse
import com.example.taskly.data.auth.model.RefreshRequest
import com.example.taskly.data.auth.model.RefreshResponse
import com.example.taskly.data.auth.model.UserResponse
import com.example.taskly.data.network.safeRequest
import com.example.taskly.domain.common.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

const val BASE_URL = "https://dummyjson.com/"
const val TOKEN_LIFETIME_MINS = 30

interface AuthApiService {
    suspend fun login(username: String, password: String): ApiResult<LoginResponse>
    suspend fun refresh(refreshToken: String): ApiResult<RefreshResponse>
    suspend fun getCurrentUser(): ApiResult<UserResponse>
}

class AppAuthApiService(
    private val plainClient: HttpClient,
    private val apiClient: HttpClient,
) : AuthApiService {

    override suspend fun login(username: String, password: String): ApiResult<LoginResponse> =
        plainClient.safeRequest<LoginResponse> {
            post("${BASE_URL}auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password, TOKEN_LIFETIME_MINS))
            }
        }

    override suspend fun refresh(refreshToken: String): ApiResult<RefreshResponse> =
        plainClient.safeRequest<RefreshResponse> {
            post("${BASE_URL}auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshRequest(refreshToken, TOKEN_LIFETIME_MINS))
            }
        }

    override suspend fun getCurrentUser(): ApiResult<UserResponse> =
        apiClient.safeRequest<UserResponse> { get("${BASE_URL}auth/me") }
}