package com.example.taskly

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform