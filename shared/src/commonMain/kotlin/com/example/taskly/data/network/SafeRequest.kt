package com.example.taskly.data.network

import com.example.taskly.domain.common.ApiError
import com.example.taskly.domain.common.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> HttpClient.safeRequest(
    crossinline block: suspend HttpClient.() -> HttpResponse,
): ApiResult<T> = try {
    val response = block()
    when (response.status.value) {
        in 200..299 -> ApiResult.Success(response.body<T>())
        400, 401, 403 -> ApiResult.Failure(ApiError.InvalidCredentials)
        in 500..599 -> ApiResult.Failure(ApiError.Server)
        else -> ApiResult.Failure(ApiError.Unknown)
    }
} catch (e: CancellationException) {
    throw e
} catch (e: SerializationException) {
    ApiResult.Failure(ApiError.Unknown)
} catch (e: Exception) {
    ApiResult.Failure(ApiError.Network)
}