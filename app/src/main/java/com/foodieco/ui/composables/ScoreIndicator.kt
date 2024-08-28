package com.foodieco.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun ScoreIndicator(
    value: Double,
    modifier: Modifier = Modifier,
    size: Int = 50
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator(
            progress = { value.toFloat() / 100 },
            strokeWidth = 5.dp,
            strokeCap = StrokeCap.Round,
            trackColor = MaterialTheme.colorScheme.surfaceDim,
            color = when(value) {
                in 0f..33f -> Color(0xFFD84654)
                in 34f..66f -> Color(0xFFF99C39)
                in 67f..100f -> Color(0xFF4F9D69)
                else -> ProgressIndicatorDefaults.circularColor
            },
            modifier = modifier.size(size.dp)
        )
        Text("${value.roundToInt()}%", fontSize = size.sp / 3)
    }
}
