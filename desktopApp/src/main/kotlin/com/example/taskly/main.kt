package com.example.taskly

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.taskly.di.initKoin

fun main() {
    initKoin { printLogger() }
    application {
        Window(onCloseRequest = ::exitApplication, title = "Taskly") {
            App()
        }
    }
}