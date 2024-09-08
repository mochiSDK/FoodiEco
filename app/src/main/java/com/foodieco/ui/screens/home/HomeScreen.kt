package com.foodieco.ui.screens.home

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.foodieco.data.database.FavoriteRecipe
import com.foodieco.data.models.SessionStatus
import com.foodieco.data.remote.OSMDataSource
import com.foodieco.data.remote.OSMRecipe
import com.foodieco.ui.UserState
import com.foodieco.ui.composables.FiltersBottomSheet
import com.foodieco.ui.composables.HomeNavigationDrawer
import com.foodieco.ui.composables.Monogram
import com.foodieco.ui.composables.NavigationRoute
import com.foodieco.ui.composables.RecipeCard
import com.foodieco.ui.screens.recipe.FavoriteRecipeState
import com.foodieco.utils.isOnline
import com.foodieco.utils.openWirelessSettings
import kotlinx.coroutines.launch

val homeScreenPadding = 8.dp
private const val MAX_RECIPES = 15

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    userState: UserState,
    osmDataSource: OSMDataSource,
    logout: (SessionStatus) -> Unit,
    favoriteRecipeState: FavoriteRecipeState,
    addRecipeToFavorites: (FavoriteRecipe) -> Unit,
    removeRecipeFromFavorites: (FavoriteRecipe) -> Unit,
    onCompose: () -> Unit
) {
    var searchBarQuery by rememberSaveable { mutableStateOf("") }
    var lastQuery by rememberSaveable { mutableStateOf("") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val toggleDrawer: () -> Unit = {
        coroutineScope.launch {
            drawerState.apply { if (isClosed) open() else close() }
        }
    }

    val ctx = LocalContext.current

    var recipes by rememberSaveable { mutableStateOf<List<OSMRecipe>?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }
    fun searchRecipe(
        ingredients: String,
        cuisines: String = "",
        diets: String = "",
        intolerances: String = ""
    ) = coroutineScope.launch {
        if (isOnline(ctx)) {
            val result = osmDataSource.searchRecipes(
                ingredients,
                cuisines,
                diets,
                intolerances,
                MAX_RECIPES
            )
            if (result == null) {
                snackBarHostState.showSnackbar(
                    message = "An error has occurred while trying to fetch recipes, try again",
                    duration = SnackbarDuration.Long
                )
                return@launch
            }
            recipes = result
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

    HomeNavigationDrawer(
        drawerState = drawerState,
        toggleDrawer = toggleDrawer,
        favoriteRecipeState = favoriteRecipeState,
        logout = logout,
        navController = navController
    ) {
        val focusManager = LocalFocusManager.current
        var showFiltersSheet by remember { mutableStateOf(false) }
        var cuisinesFilters by remember { mutableStateOf<List<String>>(emptyList()) }
        var dietsFilters by remember { mutableStateOf<List<String>>(emptyList()) }
        var intolerancesFilters by remember { mutableStateOf<List<String>>(emptyList()) }
        val listState = rememberLazyListState()
        Scaffold(
            topBar = {
                SearchBar(
                    placeholder = { Text("What's in the fridge?") },
                    query = searchBarQuery,
                    onQueryChange = {
                        searchBarQuery = it
                        if (searchBarQuery.isEmpty()) {
                            recipes = emptyList()
                        }
                    },
                    onSearch = {
                        searchRecipe(
                            it,
                            cuisinesFilters.joinToString(", "),
                            dietsFilters.joinToString(", "),
                            intolerancesFilters.joinToString(", ")
                        )
                        lastQuery = it
                        focusManager.clearFocus()
                        coroutineScope.launch { listState.scrollToItem(0) }
                    },
                    active = false,
                    onActiveChange = {},
                    leadingIcon = {
                        IconButton(onClick = toggleDrawer) {
                            Icon(Icons.Outlined.Menu, "Menu icon")
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { navController.navigate(NavigationRoute.Profile.route) },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            if (userState.profilePicture.toUri() != Uri.EMPTY) {
                                SubcomposeAsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(userState.profilePicture)
                                        .crossfade(true)
                                        .crossfade(1000)
                                        .build(),
                                    contentDescription = "Profile picture",
                                    contentScale = ContentScale.Crop,
                                    loading = { CircularProgressIndicator() },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                )
                            } else {
                                Monogram(text = userState.username[0].toString(), size = 48.dp, modifier = Modifier.clip(CircleShape))
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(homeScreenPadding)
                        .fillMaxWidth()
                ) { }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showFiltersSheet = true }) {
                    Icon(Icons.Outlined.FilterList, "Filter icon")
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { innerPadding ->
            var showLoadingIndicator by remember { mutableStateOf(true) }
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(homeScreenPadding)
            ) {
                item {
                    AnimatedVisibility(
                        visible = showLoadingIndicator,
                        enter = slideInVertically(),
                        exit = slideOutVertically()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                recipes?.let {
                    items(it) { recipe ->
                        LaunchedEffect(Unit) { showLoadingIndicator = false }
                        RecipeCard(
                            navController = navController,
                            recipeId = recipe.id.toString(),
                            title = recipe.title,
                            subtext = recipe.cuisines.joinToString(", "),
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
        if (showFiltersSheet) {
            FiltersBottomSheet(
                onDismissRequest = { showFiltersSheet = false },
                selectedCuisinesFilters = cuisinesFilters,
                addToSelectedCuisinesFilters = { cuisinesFilters += it },
                removeFromSelectedCuisinesFilters = { cuisinesFilters -= it },
                selectedDietsFilters = dietsFilters,
                addToSelectedDietsFilters = { dietsFilters += it },
                removeFromSelectedDietsFilters = { dietsFilters -= it },
                selectedIntolerancesFilters = intolerancesFilters,
                addToSelectedIntolerancesFilters = { intolerancesFilters += it },
                removeFromSelectedIntolerancesFilters = { intolerancesFilters -= it },
                clearSelectedFilters = {
                    cuisinesFilters = emptyList()
                    dietsFilters = emptyList()
                    intolerancesFilters = emptyList()
                }
            )
        }
        LaunchedEffect(cuisinesFilters, dietsFilters, intolerancesFilters) {
            if (searchBarQuery.isEmpty()) {
                recipes = osmDataSource.getRandomRecipes(
                    (cuisinesFilters + dietsFilters + intolerancesFilters).joinToString(", "),
                    number = MAX_RECIPES
                )
            }
            if (searchBarQuery.isNotBlank()) {
                searchRecipe(
                    lastQuery,
                    cuisinesFilters.joinToString(", "),
                    dietsFilters.joinToString(", "),
                    intolerancesFilters.joinToString(", ")
                )
            }
        }
        LaunchedEffect(Unit) {
            onCompose()
        }
    }
}
