package com.example.taskly.data.auth.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val username: String, val firstName: String)