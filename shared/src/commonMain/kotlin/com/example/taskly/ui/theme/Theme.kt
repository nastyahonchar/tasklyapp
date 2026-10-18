package com.example.taskly.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Primary, onPrimary = OnPrimary,
    secondary = Tertiary, onSecondary = OnTertiary,
    secondaryContainer = Secondary, onSecondaryContainer = OnSecondary,
    tertiary = Tertiary, onTertiary = OnTertiary,
    background = Background, onBackground = Neutral,
    surface = Surface, onSurface = Neutral,
    surfaceVariant = SurfaceVariant, onSurfaceVariant = Outline,
    outline = Outline, error = ErrorRed,
)

@Composable
fun TasklyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = tasklyTypography(),
        content = content,
    )
}