package com.foodieco.ui.composables

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RoomService
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.foodieco.data.remote.OSMRecipeDetails
import com.foodieco.ui.theme.capriolaFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun RecipeBanner(
    recipe: OSMRecipeDetails,
    onTitleDisappearCoordinate: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var isBannerFlipped by remember { mutableStateOf(false) }
    val flipBanner = { isBannerFlipped = !isBannerFlipped }
    AnimatedContent(
        targetState = isBannerFlipped,
        label = "Banner flip animation",
        transitionSpec = {
            if (targetState > initialState) {
                slideInVertically { height -> height } togetherWith slideOutVertically { height -> height }
            } else {
                slideInVertically { height -> height } togetherWith slideOutVertically { height -> height }
            }
        }
    ) { flipped ->
        when {
            flipped -> RecipeBannerBack(
                recipe = recipe,
                onClick = flipBanner,
                modifier = modifier
            )
            else -> RecipeBannerFront(
                recipe = recipe,
                onClick = flipBanner,
                onTitleDisappearance = onTitleDisappearCoordinate,
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecipeBannerFront(
    recipe: OSMRecipeDetails,
    onClick: () -> Unit,
    onTitleDisappearance: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var showImageAlert by remember { mutableStateOf(false) }
    if (showImageAlert) {
        Dialog(onDismissRequest = { showImageAlert = false }) {
            Box(modifier = Modifier.clip(RoundedCornerShape(16.dp))) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(recipe.image)
                        .build(),
                    contentDescription = "Recipe image",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    Box(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                when {
                    recipe.image.isNullOrEmpty() -> MaterialTheme.colorScheme.primary
                    else -> Color.Transparent
                }
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = { showImageAlert = true }
            ),
    ) {
        if (!recipe.image.isNullOrEmpty()) {
            var startAnimation by remember { mutableStateOf(false) }
            val alpha by animateFloatAsState(
                targetValue = if (startAnimation) 0.6f else 1f,
                animationSpec = tween(5000),
                label = "Animated alpha"
            )
            val blur by animateDpAsState(
                targetValue = if (startAnimation) 5.dp else 0.dp,
                animationSpec = tween(5000),
                label = "Animated blur"
            )
            LaunchedEffect(Unit) { startAnimation = true }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipe.image)
                    .build(),
                contentDescription = "Recipe image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .alpha(alpha)
                    .blur(blur)
            )
        }
        var isBannerTextVisible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            isBannerTextVisible = true
        }
        AnimatedVisibility(
            visible = isBannerTextVisible,
            enter = when {
                recipe.image.isNullOrEmpty() -> fadeIn()
                else -> fadeIn(animationSpec = tween(durationMillis = 1000, delayMillis = 2000))
            }
        ) {
            Box {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        recipe.title,
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        fontFamily = capriolaFontFamily,
                        color = Color.White,
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            onTitleDisappearance(coordinates.positionInRoot().y)
                        }
                    )
                    Row {
                        Row(modifier = Modifier.padding(4.dp)) {
                            Icon(
                                Icons.Outlined.RoomService,
                                "Servings icon",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${recipe.servings}",
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Row(modifier = Modifier.padding(4.dp)) {
                            Icon(
                                Icons.Outlined.Timer,
                                "Timer icon",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${recipe.readyInMinutes}'",
                                color = Color.White
                            )
                        }
                    }
                }
                Text(
                    recipe.types.joinToString(", ").capitalize(Locale.current),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(6.dp)
                )
            }
        }
    }
}

@Composable
private fun RecipeBannerBack(
    recipe: OSMRecipeDetails,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var imageDominantColor by remember { mutableStateOf(Color.Transparent) }

    val ctx = LocalContext.current
    suspend fun loadImageBitmap(imageUrl: String): Bitmap? {
        val loader = ImageLoader(ctx)
        val request = ImageRequest.Builder(ctx)
            .data(imageUrl)
            .allowHardware(false)
            .build()
        return when (val result = loader.execute(request)) {
            is SuccessResult -> result.drawable.toBitmap()
            else -> null
        }
    }

    LaunchedEffect(Unit) {
        recipe.image?.let { imageUrl ->
            val bitmap = withContext(Dispatchers.IO) { loadImageBitmap(imageUrl) }
            bitmap?.let {
                val palette = Palette.from(it).generate()
                imageDominantColor = Color(palette.getDominantColor(Color.Transparent.toArgb()))
            }
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(imageDominantColor)
            .height(200.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
        ) {
            CircularIndicator(
                recipe.nutrition.caloricBreakdown.percentProtein,
                subText = "Protein",
                color = Color(0xFFF15BB5),
                size = 60,
                modifier = Modifier.padding(4.dp)
            )
            CircularIndicator(
                recipe.nutrition.caloricBreakdown.percentFat,
                subText = "Fat",
                color = Color(0xFF9B5DE5),
                size = 60,
                modifier = Modifier.padding(4.dp)
            )
            CircularIndicator(
                recipe.nutrition.caloricBreakdown.percentCarbs,
                subText = "Carbs",
                color = Color(0xFF4895EF),
                size = 60,
                modifier = Modifier.padding(4.dp)
            )
            VerticalDivider(
                thickness = 2.dp,
                color = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.padding(top = 60.dp, bottom = 60.dp, start = 8.dp, end = 8.dp)
            )
            CircularIndicator(
                recipe.score,
                subText = "Score",
                color = when (recipe.score) {
                    in 0f..33f -> Color(0xFFD84654)
                    in 34f..66f -> Color(0xFFF99C39)
                    in 67f..100f -> Color(0xFF4F9D69)
                    else -> null
                },
                size = 60,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
