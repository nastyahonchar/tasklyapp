package com.example.taskly.domain.repository

interface OnboardingRepository {
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted(completed: Boolean)
}