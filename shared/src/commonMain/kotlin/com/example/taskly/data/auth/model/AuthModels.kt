package com.example.taskly.data.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String, val expiresInMins: Int)

@Serializable
data class RefreshRequest(val refreshToken: String, val expiresInMins: Int)

@Serializable
data class LoginResponse(val accessToken: String, val refreshToken: String)

@Serializable
data class RefreshResponse(val accessToken: String, val refreshToken: String)

@Serializable
data class UserResponse(val username: String, val firstName: String)