package com.example.cvmakerapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// NOTE: To exactly match the design, add the "Inter" and "JetBrains Mono"
// variable fonts to res/font and swap FontFamily.Default / FontFamily.Monospace
// below for FontFamily(Font(R.font.inter_...)) etc.
val InterFontFamily = FontFamily.Default
val JetBrainsMonoFontFamily = FontFamily.Monospace

// Extra text styles beyond the default Material3 slots — use these directly
// where the design calls for "display" or the mono "label-sm" data style.
object CvTextStyles {
    val display = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold, // 700
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = (-0.02).em
    )
    val labelMono = TextStyle(
        fontFamily = JetBrainsMonoFontFamily,
        fontWeight = FontWeight.Medium, // 500
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.05.em
    )
}

val CvTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold, // 600
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.01).em
    ),
    headlineMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium, // 500
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 28.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = JetBrainsMonoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.05.em
    )
)
