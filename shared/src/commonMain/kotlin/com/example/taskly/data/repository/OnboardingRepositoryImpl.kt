package com.example.taskly.data.repository

import com.example.taskly.data.AppSettings
import com.example.taskly.domain.repository.OnboardingRepository

class OnboardingRepositoryImpl(private val settings: AppSettings) : OnboardingRepository {
    override fun isOnboardingCompleted(): Boolean = settings.onboardingCompleted
    override fun setOnboardingCompleted(completed: Boolean) {
        settings.onboardingCompleted = completed
    }
}