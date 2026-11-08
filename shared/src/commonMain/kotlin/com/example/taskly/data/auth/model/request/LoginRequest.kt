package com.example.taskly.data.auth.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int,
)