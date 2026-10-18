package com.example.taskly.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import taskly.shared.generated.resources.*

@Composable
fun interFontFamily() = FontFamily(
    Font(Res.font.inter_regular, FontWeight.Normal),
    Font(Res.font.inter_medium, FontWeight.Medium),
    Font(Res.font.inter_semibold, FontWeight.SemiBold),
    Font(Res.font.inter_bold, FontWeight.Bold),
)

@Composable
fun tasklyTypography(): Typography {
    val inter = interFontFamily()
    return Typography(
        headlineMedium = TextStyle(fontFamily = inter, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 34.sp),
        titleMedium = TextStyle(fontFamily = inter, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
        bodyMedium = TextStyle(fontFamily = inter, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
        labelLarge = TextStyle(fontFamily = inter, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
        labelSmall = TextStyle(fontFamily = inter, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 0.8.sp),
    )
}