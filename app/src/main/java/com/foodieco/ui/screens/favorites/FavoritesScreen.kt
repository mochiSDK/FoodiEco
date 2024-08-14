package com.foodieco.ui.screens.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavHostController) {
    var isSearchBarActive by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = isSearchBarActive,
                enter = scaleIn(
                    initialScale = 0.8f,
                    transformOrigin = TransformOrigin(1f, 0f)
                ) + fadeIn(),
                exit = scaleOut(
                    targetScale = 0.8f,
                    transformOrigin = TransformOrigin(1f, 0f)
                ) + fadeOut(),
                modifier = Modifier.zIndex(1f)
            ) {
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
            AnimatedVisibility(
                visible = !isSearchBarActive,
                enter = expandVertically(animationSpec = spring(stiffness = Spring.StiffnessLow)) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
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
