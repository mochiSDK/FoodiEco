package com.foodieco.ui.screens.stats

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.foodieco.ui.screens.recipe.FavoriteRecipeState
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavHostController, favoriteRecipeState: FavoriteRecipeState) {
    val modelProducer = remember { CartesianChartModelProducer() }
    var cuisinesCount by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    LaunchedEffect(Unit) {
        cuisinesCount = favoriteRecipeState.recipes
            .map { it.cuisines.split(", ") }
            .flatten()
            .map { cuisine -> cuisine.ifEmpty { "Uncategorized" } }
            .groupingBy { it }
            .eachCount()
        modelProducer.runTransaction {
            columnSeries {
                series(cuisinesCount.values)
            }
        }
    }

    val cuisines = cuisinesCount.keys.toList()
    val bottomAxisValueFormatter = CartesianValueFormatter { x, _, _ ->
        cuisines[x.toInt()]
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Favorite cuisines") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Arrow back icon")
                    }
                }
            )
        }
    ) { innerPadding ->
        ProvideVicoTheme(rememberM3VicoTheme()) {
            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberColumnCartesianLayer(),
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(valueFormatter = bottomAxisValueFormatter),
                ),
                modelProducer = modelProducer,
                modifier = Modifier.padding(innerPadding).padding(12.dp)
            )
        }
    }
}
