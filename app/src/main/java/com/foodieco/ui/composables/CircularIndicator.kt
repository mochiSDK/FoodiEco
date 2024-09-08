package com.foodieco.ui.composables

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun CircularIndicator(
    value: Double,
    modifier: Modifier = Modifier,
    subText: String = "",
    color: Color? = null,
    size: Int = 50
) {
    var startAnimation by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (startAnimation) value.toFloat() / 100 else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
        label = "Animated alpha"
    )
    LaunchedEffect(Unit) { startAnimation = true }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            CircularProgressIndicator(
                progress = { progress },
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
