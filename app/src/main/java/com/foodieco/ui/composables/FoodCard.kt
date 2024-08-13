package com.foodieco.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.NoPhotography
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodieco.ui.theme.FoodiEcoTheme

@Composable
fun FoodCard(
    title: String,
    subtext: String,
    modifier: Modifier = Modifier,
    image: String? = null
) {
    ElevatedCard(
        onClick = { /*TODO*/ },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier.size(width = 340.dp, height = 80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Icon(
                    Icons.Outlined.NoPhotography,
                    "No photo icon",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.alpha(0.6f)
                        .align(Alignment.Center)
                )
            }
            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                Text(title)
                Text(subtext)
            }
            IconButton(onClick = { /*TODO*/ },) {
                Icon(Icons.Outlined.FavoriteBorder, "Favorite icon")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodCardPreview() {
    FoodiEcoTheme {
        FoodCard("Title", "Subtext")
    }
}
