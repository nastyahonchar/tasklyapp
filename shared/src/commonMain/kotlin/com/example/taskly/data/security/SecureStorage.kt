package com.example.taskly.data.security

import eu.anifantakis.lib.ksafe.KSafe

private const val ACCESS_TOKEN_KEY = "accessToken"
private const val REFRESH_TOKEN_KEY = "refreshToken"

interface SecureStorage {
    suspend fun getAccessToken(): String
    suspend fun getRefreshToken(): String
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens()
}

class AppSecureStorage(private val ksafe: KSafe) : SecureStorage {
    override suspend fun getAccessToken(): String = ksafe.get(ACCESS_TOKEN_KEY, "")
    override suspend fun getRefreshToken(): String = ksafe.get(REFRESH_TOKEN_KEY, "")

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        ksafe.put(ACCESS_TOKEN_KEY, accessToken)
        ksafe.put(REFRESH_TOKEN_KEY, refreshToken)
    }

    override suspend fun clearTokens() {
        ksafe.put(ACCESS_TOKEN_KEY, "")
        ksafe.put(REFRESH_TOKEN_KEY, "")
    }
}