package com.example.taskly.data.auth.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(val accessToken: String, val refreshToken: String)