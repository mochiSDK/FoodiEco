package com.foodieco.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodieco.ui.theme.FoodiEcoTheme

@Composable
fun Monogram(text: String, size: Dp) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
            .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
    ) {
        Text(
            text = text[0].toString().toUpperCase(Locale.current),
            style = TextStyle(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = size.value.sp / 4,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MonogramPreview() {
    FoodiEcoTheme {
        Monogram(text = "a", 100.dp)
    }
}
