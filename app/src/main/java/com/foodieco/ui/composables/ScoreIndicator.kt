package com.foodieco.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodieco.ui.theme.capriolaFontFamily
import kotlin.math.roundToInt

@Composable
fun ScoreIndicator(
    value: Double,
    modifier: Modifier = Modifier,
    subText: String = "",
    color: Color? = null,
    size: Int = 50
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            CircularProgressIndicator(
                progress = { value.toFloat() / 100 },
                strokeWidth = (size / 8).dp,
                strokeCap = StrokeCap.Round,
                trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                color = color ?: ProgressIndicatorDefaults.circularColor,
                modifier = modifier.size(size.dp)
            )
            Text(
                "${value.roundToInt()}%",
                fontSize = size.sp / 4,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.surface
            )
        }
        if (subText.isNotEmpty()) {
            Text(
                subText,
                fontFamily = capriolaFontFamily,
                color = MaterialTheme.colorScheme.surface
            )
        }
    }
}
