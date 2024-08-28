package com.foodieco.ui.screens.recipe

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.RoomService
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.foodieco.data.remote.OSMDataSource
import com.foodieco.data.remote.OSMRecipeDetails
import com.foodieco.ui.theme.capriolaFontFamily
import com.foodieco.utils.isOnline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetails(
    navController: NavHostController,
    recipeId: String?,
    osmDataSource: OSMDataSource
) {
    if (recipeId == null) {
        throw IllegalStateException("Recipe ID was null")
    }
    var isFavorite by remember { mutableStateOf(false) }
    var isBoxFlipped by remember { mutableStateOf(false) }
    var recipe by remember { mutableStateOf<OSMRecipeDetails?>(null) }
    val ctx = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (isOnline(ctx)) {
            recipe = osmDataSource.searchRecipeById(recipeId.toInt())
        } else {
            snackBarHostState.showSnackbar(
                message = "An error has occurred while trying to open the recipe",
                duration = SnackbarDuration.Long
            )
            navController.navigateUp()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Arrow back icon")
                    }
                },
                actions = {
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        AnimatedContent(
                            targetState = isFavorite,
                            label = "Bouncing scale in transition",
                            transitionSpec = {
                                scaleIn(
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow)
                                ) togetherWith scaleOut()
                            }
                        ) { favorite ->
                            when {
                                favorite -> Icon(Icons.Outlined.Favorite, "Favorite icon")
                                else -> Icon(Icons.Outlined.FavoriteBorder, "Favorite icon")
                            }
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(8.dp)) {
            recipe?.let {
                AnimatedContent(
                    targetState = isBoxFlipped,
                    label = "",
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInVertically { height -> height } togetherWith slideOutVertically { height -> height }
                        } else {
                            slideInVertically { height -> height } togetherWith slideOutVertically { height -> height }
                        }
                    }
                ) { flipped ->
                    if (flipped) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .height(200.dp)
                                .fillMaxWidth()
                                .clickable { isBoxFlipped = !isBoxFlipped },
                            contentAlignment = Alignment.Center
                        ) {

                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { isBoxFlipped = !isBoxFlipped },
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.image)
                                    .build(),
                                contentDescription = "Recipe image",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black)
                                    .alpha(0.6f)
                                    .blur(5.dp)
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    it.title,
                                    fontSize = 22.sp,
                                    fontFamily = capriolaFontFamily,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
//                        Text(
//                            it.types.joinToString(", ").capitalize(Locale.current),
//                            color = MaterialTheme.colorScheme.onPrimary
//                        )
                                Row {
                                    Row(modifier = Modifier.padding(4.dp)) {
                                        Icon(
                                            Icons.Outlined.RoomService,
                                            "Servings icon",
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            "${it.servings}",
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Row(modifier = Modifier.padding(4.dp)) {
                                        Icon(
                                            Icons.Outlined.Timer,
                                            "Timer icon",
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            "${it.readyInMinutes}'",
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
//                        ScoreIndicator(it.score)
                                }
                            }
                        }
                    }
                }
                Text("Ingredients:")
                it.ingredients.forEach { ingredient ->
                    Text("${ingredient.measures.metric.amount} ${ingredient.measures.metric.unit} ${ingredient.name}")
                }
                Text("Instructions: ${it.instructions}")
            }
        }
    }
}
