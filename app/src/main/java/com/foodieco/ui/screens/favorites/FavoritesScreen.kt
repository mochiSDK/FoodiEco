package com.foodieco.ui.screens.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.foodieco.ui.theme.FoodiEcoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen() {
    var isSearchBarActive by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            AnimatedVisibility(visible = isSearchBarActive, enter = fadeIn(), exit = fadeOut()) {
                SearchBar(
                    query = "",
                    onQueryChange = { /*TODO*/ },
                    onSearch = { /*TODO*/ },
                    active = true,
                    onActiveChange = { /*TODO*/ },
                    leadingIcon = {
                        IconButton(onClick = { isSearchBarActive = false }) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Arrow back icon")
                        }
                    }
                ) {

                }
            }
            AnimatedVisibility(visible = !isSearchBarActive, enter = fadeIn(), exit = fadeOut()) {
                LargeTopAppBar(
                    title = { Text("Favorites") },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Back arrow button")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchBarActive = true }) {
                            Icon(Icons.Outlined.Search, "Search icon")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // TODO: insert lazy list of food cards.
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesPreview() {
    FoodiEcoTheme {
        FavoritesScreen()
    }
}
