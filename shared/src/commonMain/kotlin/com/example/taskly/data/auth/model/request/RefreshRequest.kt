package com.example.taskly.data.auth.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val refreshToken: String,
    val expiresInMins: Int,
)