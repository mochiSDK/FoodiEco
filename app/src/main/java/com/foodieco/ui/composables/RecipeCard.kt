package com.foodieco.ui.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.NoPhotography
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.foodieco.data.database.FavoriteRecipe
import com.foodieco.ui.screens.recipe.FavoriteRecipeState

val imageSize = 80.dp

@Composable
fun RecipeCard(
    navController: NavHostController,
    recipeId: String,
    title: String,
    subtext: String,
    favoriteRecipeState: FavoriteRecipeState,
    addToFavorites: (FavoriteRecipe) -> Unit,
    removeFromFavorites: (FavoriteRecipe) -> Unit,
    modifier: Modifier = Modifier,
    image: String? = null
) {
    var isFavorite by remember { mutableStateOf(false) }
    
    LaunchedEffect(recipeId) {
        isFavorite = favoriteRecipeState.recipes.any { it.id == recipeId.toInt() }
    }
    
    ElevatedCard(
        onClick = {
            navController.navigate(NavigationRoute.RecipeDetails.buildRoute(recipeId))
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier.size(width = 340.dp, height = 80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(imageSize)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                when {
                    image == null -> Icon(
                        Icons.Outlined.NoPhotography,
                        "No photo icon",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .alpha(0.6f)
                            .align(Alignment.Center)
                    )
                    else -> AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(image)
                            .build(),
                        contentDescription = "Recipe image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(imageSize)
                    )
                }
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(16.dp)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtext)
            }
            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    val recipe = FavoriteRecipe(
                        recipeId.toInt(),
                        title,
                        image,
                        subtext
                    )
                    when {
                        isFavorite -> addToFavorites(recipe)
                        else -> removeFromFavorites(recipe)
                    }
                }
            ) {
                AnimatedContent(
                    targetState = isFavorite,
                    label = "Bouncing scale in transition",
                    transitionSpec = {
                        scaleIn(
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow)
                        ) togetherWith scaleOut()
                    }
                ) {
                    if (it) {
                        Icon(Icons.Outlined.Favorite, "Favorite icon")
                    } else {
                        Icon(Icons.Outlined.FavoriteBorder, "Favorite border icon")
                    }
                }
            }
        }
    }
}
