package com.foodieco.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class Point(val radius: Dp, val color: Color)

data class Line(val strokeWidth: Dp, val color: Color)

enum class TimelineNodePosition {
    START,
    END
}

@Composable
fun NodeContent(content: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
        modifier = modifier.wrapContentSize()
    ) {
        Text(content, modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun TimelineNode(
    position: TimelineNodePosition,
    modifier: Modifier = Modifier,
    point: Point,
    contentStartPadding: Dp = PointDefaults.pointStyle().radius * 4,
    contentBetweenPadding: Dp = 32.dp,
    onHeightMeasured: (Int) -> Unit,
    content: @Composable BoxScope.(modifier: Modifier) -> Unit
) {
    var contentHeightPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    Box(
        modifier = modifier
            .drawBehind {
                val circleRadiusPx = point.radius.toPx()
                // Center Y coordinate based on the content height
                val centerY = contentHeightPx.toFloat() / 2
                drawCircle(
                    color = point.color,
                    radius = circleRadiusPx,
                    center = Offset(circleRadiusPx, centerY)
                )
                drawCircle(
                    color = Color.White,
                    radius = circleRadiusPx - 12,
                    center = Offset(circleRadiusPx, centerY)
                )
            }
    ) {
        content(
            Modifier
                .onGloballyPositioned { coordinates ->
                    contentHeightPx = when {
                        position != TimelineNodePosition.END -> coordinates.size.height - dpToPx(contentBetweenPadding, density)
                        else -> coordinates.size.height
                    }
                    onHeightMeasured(contentHeightPx)
                }
                .padding(
                    start = contentStartPadding,
                    bottom = when {
                        position != TimelineNodePosition.END -> contentBetweenPadding
                        else -> 0.dp
                    }
                )
        )
    }
}

object LineDefaults {
    @Composable
    fun lineStyle(
        strokeWidth: Dp = 2.dp,
        color: Color = MaterialTheme.colorScheme.primary
    ) = Line(strokeWidth, color)
}

object PointDefaults {
    private val defaultCircleRadius = 8.dp
    @Composable
    fun pointStyle(
        radius: Dp = defaultCircleRadius,
        backgroundColor: Color = MaterialTheme.colorScheme.primary
    ) = Point(radius, backgroundColor)
}

@Composable
fun Timeline(
    items: List<String>,
    point: Point = PointDefaults.pointStyle(),
    line: Line = LineDefaults.lineStyle(),
) {
    var nodeHeight by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier.padding(16.dp)
            .drawBehind {
                drawLine(
                    color = line.color,
                    strokeWidth = line.strokeWidth.toPx(),
                    start = Offset(point.radius.toPx(), point.radius.toPx() * 2),
                    end = Offset(point.radius.toPx(), this.size.height - nodeHeight / 2)
                )
            }
    ) {
        items.forEachIndexed { index, item ->
            TimelineNode(
                position = when {
                    index != items.size - 1 -> TimelineNodePosition.START
                    else -> TimelineNodePosition.END
                },
                point = point,
                onHeightMeasured = { nodeHeight = it }
            ) { modifier ->
                NodeContent(item, modifier)
            }
        }
    }
}

private fun dpToPx(dp: Dp, density: Density): Int {
    return (dp.value * density.density + 0.5f).toInt()
}
