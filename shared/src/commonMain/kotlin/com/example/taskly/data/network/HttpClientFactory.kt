package com.example.taskly.data.network

import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient = HttpClient {
    install(Logging) {
        level = LogLevel.INFO
        logger = object : Logger {
            override fun log(message: String) {
                KermitLogger.withTag("Ktor").d { message }
            }
        }
    }
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true; isLenient = true })
    }
    config()
}