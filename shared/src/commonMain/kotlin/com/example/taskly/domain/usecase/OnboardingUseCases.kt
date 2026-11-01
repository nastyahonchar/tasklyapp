package com.example.taskly.domain.usecase

import com.example.taskly.domain.repository.OnboardingRepository

class IsOnboardingCompletedUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(): Boolean = repository.isOnboardingCompleted()
}

class SetOnboardingCompletedUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(completed: Boolean) = repository.setOnboardingCompleted(completed)
}