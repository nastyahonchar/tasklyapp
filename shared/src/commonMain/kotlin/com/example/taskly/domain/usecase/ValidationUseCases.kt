package com.example.taskly.domain.usecase

enum class ValidationError { Empty, TooShort, InvalidChars }

private const val USERNAME_MIN_LENGTH = 3
private const val PASSWORD_MIN_LENGTH = 6

private const val USERNAME_ALLOWED_SYMBOLS = "_.-@"

class ValidateUsernameUseCase {
    operator fun invoke(value: String): ValidationError? {
        val v = value.trim()
        return when {
            v.isEmpty() -> ValidationError.Empty
            v.length < USERNAME_MIN_LENGTH -> ValidationError.TooShort
            !v.all { it.isLetterOrDigit() || it in USERNAME_ALLOWED_SYMBOLS } -> ValidationError.InvalidChars
            else -> null
        }
    }
}

class ValidatePasswordUseCase {
    operator fun invoke(value: String): ValidationError? = when {
        value.isBlank() -> ValidationError.Empty
        value.length < PASSWORD_MIN_LENGTH -> ValidationError.TooShort
        else -> null
    }
}