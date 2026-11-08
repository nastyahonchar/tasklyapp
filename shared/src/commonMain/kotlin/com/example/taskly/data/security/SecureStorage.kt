package com.example.taskly.data.security

internal const val ACCESS_TOKEN_KEY = "accessToken"
internal const val REFRESH_TOKEN_KEY = "refreshToken"

interface SecureStorage {
    suspend fun getAccessToken(): String
    suspend fun getRefreshToken(): String
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens()
}