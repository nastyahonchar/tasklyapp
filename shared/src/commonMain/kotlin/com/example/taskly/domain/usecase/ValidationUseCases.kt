package com.example.taskly.domain.usecase

enum class ValidationError { Empty, TooShort, InvalidChars }

class ValidateUsernameUseCase {
    operator fun invoke(value: String): ValidationError? {
        val v = value.trim()
        return when {
            v.isEmpty() -> ValidationError.Empty
            v.length < 3 -> ValidationError.TooShort
            !v.all { it.isLetterOrDigit() || it == '_' || it == '.' } -> ValidationError.InvalidChars
            else -> null
        }
    }
}

class ValidatePasswordUseCase {
    operator fun invoke(value: String): ValidationError? = when {
        value.isEmpty() -> ValidationError.Empty
        value.length < 6 -> ValidationError.TooShort
        else -> null
    }
}