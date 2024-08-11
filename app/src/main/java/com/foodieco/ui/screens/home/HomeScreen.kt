package com.foodieco.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodieco.ui.composables.Monogram
import com.foodieco.ui.theme.FoodiEcoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            SearchBar(
                placeholder = { Text("What's in the frige?") },
                query = "",
                onQueryChange = {},
                onSearch = {},
                active = false,
                onActiveChange = {},
                leadingIcon = { IconButton(onClick = { /*TODO*/ }) { Icon(Icons.Outlined.Menu, "Menu icon") } },
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Outlined.Search, "Menu icon")
                        Spacer(modifier = Modifier.width(14.dp))
                        IconButton(onClick = { /*TODO*/ }) {
                            Monogram(text = "A", size = 48.dp, modifier = Modifier.clip(CircleShape))
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
            ) {

            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FoodiEcoTheme {
        HomeScreen()
    }
}
