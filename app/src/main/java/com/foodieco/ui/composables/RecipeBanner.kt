package com.foodieco.ui.composables

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                modifier = modifier
            )
        }
    }
}

@Composable
private fun RecipeBannerFront(
    recipe: OSMRecipeDetails,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(recipe.image)
                .build(),
            contentDescription = "Recipe image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .alpha(0.6f)
                .blur(5.dp)
        )
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
                color = MaterialTheme.colorScheme.onPrimary
            )
            Row {
                Row(modifier = Modifier.padding(4.dp)) {
                    Icon(
                        Icons.Outlined.RoomService,
                        "Servings icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${recipe.servings}",
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
                        "${recipe.readyInMinutes}'",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        Text(
            recipe.types.joinToString(", ").capitalize(Locale.current),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(6.dp)
        )
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
        recipe.image.let { imageUrl ->
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
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
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
