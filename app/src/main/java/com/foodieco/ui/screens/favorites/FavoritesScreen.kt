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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.foodieco.data.database.FavoriteRecipe
import com.foodieco.ui.composables.NavigationRoute
import com.foodieco.ui.composables.RecipeCard
import com.foodieco.ui.screens.home.homeScreenPadding
import com.foodieco.ui.screens.recipe.FavoriteRecipeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavHostController,
    favoriteRecipeState: FavoriteRecipeState,
    addRecipeToFavorites: (FavoriteRecipe) -> Unit,
    removeRecipeFromFavorites: (FavoriteRecipe) -> Unit
) {
    var isSearchBarActive by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf<List<FavoriteRecipe>>(emptyList()) }
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
                    query = query,
                    onQueryChange = {
                        query = it
                        suggestions = favoriteRecipeState.recipes.filter { recipe ->
                            recipe.title.contains(query, ignoreCase = true)
                        }
                        if (query.isBlank()) {
                            suggestions = emptyList()
                        }
                    },
                    onSearch = { },
                    active = true,
                    onActiveChange = { },
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                isSearchBarActive = false
                                query = ""
                                suggestions = emptyList()
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Arrow back icon")
                        }
                    },
                    placeholder = { Text("Search favorites") }
                ) {
                    LazyColumn {
                        items(suggestions) {
                            DropdownMenuItem(
                                text = { Text(it.title) },
                                leadingIcon = { Icon(Icons.Outlined.Search, "Search icon") },
                                onClick = {
                                    navController.navigate(
                                        NavigationRoute.RecipeDetails.buildRoute(it.id.toString())
                                    )
                                }
                            )
                        }
                    }
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
                        IconButton(onClick = { navController.navigateUp() }) {
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
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
                .padding(homeScreenPadding)
        ) {
            items(favoriteRecipeState.recipes) { recipe ->
                RecipeCard(
                    navController = navController,
                    recipeId = recipe.id.toString(),
                    title = recipe.title,
                    subtext = recipe.cuisines,
                    image = recipe.image,
                    favoriteRecipeState = favoriteRecipeState,
                    addToFavorites = addRecipeToFavorites,
                    removeFromFavorites = removeRecipeFromFavorites,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}
