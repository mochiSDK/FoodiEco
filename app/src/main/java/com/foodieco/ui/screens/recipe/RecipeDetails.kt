package com.foodieco.ui.screens.recipe

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.foodieco.data.remote.OSMDataSource
import com.foodieco.data.remote.OSMRecipeDetails
import com.foodieco.ui.composables.RecipeBanner
import com.foodieco.ui.theme.capriolaFontFamily
import com.foodieco.utils.isOnline
import com.foodieco.utils.openWirelessSettings
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeDefaults
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults

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
    var recipeDetails by remember { mutableStateOf<OSMRecipeDetails?>(null) }
    val ctx = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (isOnline(ctx)) {
            recipeDetails = osmDataSource.searchRecipeById(recipeId.toInt())
            if (recipeDetails == null) {
                snackBarHostState.showSnackbar(
                    message = "An error has occurred while trying to open the recipe",
                    duration = SnackbarDuration.Long
                )
                navController.navigateUp()
            }
        } else {
            val snackbarResult = snackBarHostState.showSnackbar(
                message = "No Internet connection",
                actionLabel = "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                openWirelessSettings(ctx)
            }
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
//            .verticalScroll(rememberScrollState())    // TODO: can't work bc the lazy column in nested. Refactor
        ) {
            val recipe = recipeDetails ?: return@Scaffold
            RecipeBanner(recipe)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Learn how to prepare ${recipe.title}, " +
                    "a delicious meal to be used as ${recipe.types.joinToString(", ")} " +
                    "by ${recipe.credits}.")
            Spacer(modifier = Modifier.height(8.dp))
            // Ingredients container.
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceDim)
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Text(
                    "Ingredients",
                    fontSize = 20.sp,
                    fontFamily = capriolaFontFamily,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(
                    thickness = 2.dp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 4.dp)
                )
                recipe.ingredients.forEach { ingredient ->
                    Row {
                        Text(
                            "${ingredient.measures.metric.amount}"
                                .dropLastWhile { digit -> digit == '0' }
                                .dropLastWhile { digit -> digit == '.' }
                        )
                        val unit = ingredient.measures.metric.unit
                        if (unit.isNotBlank()) {
                            Text(" $unit")
                        }
                        Text(" ${ingredient.name}")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Instructions timeline.
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Text(
                    "Instructions",
                    fontSize = 20.sp,
                    fontFamily = capriolaFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                recipe.instructions.forEach { instruction ->
                    if (instruction.name.isNotEmpty()) {
                        Text(instruction.name)
                    }
                    JetLimeColumn(
                        itemsList = ItemsList(instruction.steps),
                        contentPadding = PaddingValues(16.dp),
                        style = JetLimeDefaults.columnStyle(
                            itemSpacing = 40.dp,
                            contentDistance = 40.dp,
                            lineThickness = 2.dp,
                            lineBrush = JetLimeDefaults.lineSolidBrush(MaterialTheme.colorScheme.surfaceDim)
                        ),
                    ) { _, item, position ->
                        JetLimeEvent(
                            style = JetLimeEventDefaults.eventStyle(
                                position = position,
                                pointColor = MaterialTheme.colorScheme.surface,
                            )
                        ) {
                            Text(item.step)
                        }
                    }
                }
            }
        }
    }
}
