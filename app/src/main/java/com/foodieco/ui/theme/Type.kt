package com.foodieco.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.foodieco.R

val cabinCondensedFontFamily = FontFamily(
    Font(R.font.cabin_condensed_regular, FontWeight.Normal),
    Font(R.font.cabin_condensed_medium, FontWeight.Medium),
    Font(R.font.cabin_condensed_semi_bold, FontWeight.SemiBold),
    Font(R.font.cabin_condensed_bold, FontWeight.Bold)
)

val capriolaFontFamily = FontFamily(
    Font(R.font.capriola_regular, FontWeight.Normal)
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = capriolaFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = capriolaFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = capriolaFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = capriolaFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = capriolaFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = capriolaFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = capriolaFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = capriolaFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = capriolaFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = cabinCondensedFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = cabinCondensedFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = cabinCondensedFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = cabinCondensedFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = cabinCondensedFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = cabinCondensedFontFamily),
)
