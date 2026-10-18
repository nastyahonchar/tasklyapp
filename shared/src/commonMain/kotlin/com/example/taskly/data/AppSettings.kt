package com.example.taskly.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class AppSettings(private val settings: Settings = Settings()) {

    var onboardingCompleted: Boolean
        get() = settings["onboarding_completed"] ?: false
        set(value) { settings["onboarding_completed"] = value }
}