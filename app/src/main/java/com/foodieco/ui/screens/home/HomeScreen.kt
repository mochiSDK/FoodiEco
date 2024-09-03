package com.foodieco.ui.screens.home

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.NoMeals
import androidx.compose.material.icons.outlined.RoomService
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.foodieco.R
import com.foodieco.data.database.FavoriteRecipe
import com.foodieco.data.models.SessionStatus
import com.foodieco.data.remote.OSMDataSource
import com.foodieco.data.remote.OSMRecipe
import com.foodieco.ui.UserState
import com.foodieco.ui.composables.Monogram
import com.foodieco.ui.composables.NavigationRoute
import com.foodieco.ui.composables.RecipeCard
import com.foodieco.ui.screens.recipe.FavoriteRecipeState
import com.foodieco.ui.theme.capriolaFontFamily
import com.foodieco.utils.isOnline
import com.foodieco.utils.openWirelessSettings
import kotlinx.coroutines.launch

val homeScreenPadding = 8.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
    var isHomeSelected by remember { mutableStateOf(true) }
    var isFavoritesSelected by remember { mutableStateOf(false) }
    var isSettingsSelected by remember { mutableStateOf(false) }

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
                1
            )    // TODO: put appropriate max number
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("FoodiEco", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = { Text("Home") },
                    icon = {
                        Icon(if (isHomeSelected) Icons.Filled.Home else Icons.Outlined.Home, "Home icon")
                    },
                    selected = isHomeSelected,
                    onClick = {
                        isHomeSelected = true
                        isFavoritesSelected = false
                        isSettingsSelected = false
                        toggleDrawer()
                        navController.navigate(NavigationRoute.Home.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Favorites") },
                    icon = {
                        Icon(if (isFavoritesSelected) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder, "Favorites icon")
                    },
                    badge = { Text(favoriteRecipeState.recipes.size.toString()) },
                    selected = isFavoritesSelected,
                    onClick = {
                        isHomeSelected = false
                        isFavoritesSelected = true
                        isSettingsSelected = false
                        toggleDrawer()
                        navController.navigate(NavigationRoute.Favorites.route)
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding))
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    icon = {
                        Icon(if (isSettingsSelected) Icons.Filled.Settings else Icons.Outlined.Settings, "Settings icon")
                    },
                    selected = isSettingsSelected,
                    onClick = {
                        isHomeSelected = false
                        isFavoritesSelected = false
                        isSettingsSelected = true
                        toggleDrawer()
                        navController.navigate(NavigationRoute.Settings.route)
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                NavigationDrawerItem(
                    label = { Text("Log out") },
                    icon = {
                        Icon(Icons.AutoMirrored.Outlined.Logout, "Logout icon")
                    },
                    selected = false,
                    onClick = { logout(SessionStatus.LoggedOut) }
                )
            }
        }
    ) {
        val focusManager = LocalFocusManager.current
        var showFiltersSheet by remember { mutableStateOf(false) }
        var cuisinesFilters by remember { mutableStateOf<List<String>>(emptyList()) }
        var dietsFilters by remember { mutableStateOf<List<String>>(emptyList()) }
        var intolerancesFilters by remember { mutableStateOf<List<String>>(emptyList()) }
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
                    },
                    active = false,
                    onActiveChange = {},
                    leadingIcon = {
                        IconButton(onClick = toggleDrawer) {
                            Icon(Icons.Outlined.Menu, "Menu icon")
                        }
                    },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                            Icon(Icons.Outlined.Search, "Menu icon")
                            Spacer(modifier = Modifier.width(14.dp))
                            IconButton(onClick = { navController.navigate(NavigationRoute.Profile.route) }) {
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
                        }
                    },
                    modifier = Modifier
                        .padding(homeScreenPadding)
                        .fillMaxWidth()    // TODO: fix when in landscape mode.
                ) {

                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showFiltersSheet = true }) {
                    Icon(Icons.Outlined.FilterList, "Filter icon")
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { innerPadding ->
            var columnAppeared by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                columnAppeared = true
            }
            var showLoadingIndicator by remember { mutableStateOf(true) }
            LazyColumn(modifier = Modifier
                .padding(innerPadding)
                .padding(homeScreenPadding)
            ) {
                item {
                    AnimatedVisibility(showLoadingIndicator) {
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
                        var itemAppeared by remember { mutableStateOf(!columnAppeared) }
                        LaunchedEffect(Unit) {
                            itemAppeared = true
                            showLoadingIndicator = false
                        }
                        AnimatedVisibility(
                            visible = itemAppeared,
                            enter = expandVertically() + fadeIn()
                        ) {
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
        }
        if (showFiltersSheet) {
            ModalBottomSheet(onDismissRequest = { showFiltersSheet = false }) {
                Column(
                    Modifier
                        .padding(22.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(Modifier.padding(bottom = 6.dp)) {
                        Icon(
                            Icons.Outlined.RoomService,
                            "Cuisine icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Cuisines",
                            fontFamily = capriolaFontFamily,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    FlowRow {
                        stringArrayResource(id = R.array.cuisines).forEach { cuisine ->
                            val isSelected = cuisinesFilters.contains(cuisine)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    when {
                                        isSelected -> cuisinesFilters -= cuisine
                                        else -> cuisinesFilters += cuisine
                                    }
                                },
                                leadingIcon = {
                                    AnimatedVisibility(isSelected) {
                                        Icon(
                                            Icons.Outlined.Check,
                                            "Check icon",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                },
                                label = { Text(cuisine) },
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(top = 6.dp, bottom = 14.dp))
                    Row(Modifier.padding(bottom = 6.dp)) {
                        Icon(
                            Icons.Outlined.Kitchen,
                            "Diets icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Diets",
                            fontFamily = capriolaFontFamily,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    FlowRow {
                        stringArrayResource(id = R.array.diets).forEach { diet ->
                            val isSelected = dietsFilters.contains(diet)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    when {
                                        isSelected -> dietsFilters -= diet
                                        else -> dietsFilters += diet
                                    }
                                },
                                leadingIcon = {
                                    AnimatedVisibility(isSelected) {
                                        Icon(
                                            Icons.Outlined.Check,
                                            "Check icon",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                },
                                label = { Text(diet.capitalize(Locale.current)) },
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(top = 6.dp, bottom = 14.dp))
                    Row(Modifier.padding(bottom = 6.dp)) {
                        Icon(
                            Icons.Outlined.NoMeals,
                            "Intolerances icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Intolerances",
                            fontFamily = capriolaFontFamily,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    FlowRow {
                        stringArrayResource(id = R.array.intolerances).forEach { intolerance ->
                            val isSelected = intolerancesFilters.contains(intolerance)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    when {
                                        isSelected -> intolerancesFilters -= intolerance
                                        else -> intolerancesFilters += intolerance
                                    }
                                },
                                leadingIcon = {
                                    AnimatedVisibility(isSelected) {
                                        Icon(
                                            Icons.Outlined.Check,
                                            "Check icon",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                },
                                label = { Text(intolerance) },
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Button(
                            onClick = {
                                cuisinesFilters = emptyList()
                                dietsFilters = emptyList()
                                intolerancesFilters = emptyList()
                            }
                        ) {
                            Text("Clear all")
                        }
                    }
                }
            }
        }
        LaunchedEffect(cuisinesFilters, dietsFilters, intolerancesFilters) {
            if (searchBarQuery.isEmpty()) {
                recipes = osmDataSource.getRandomRecipes((cuisinesFilters + dietsFilters + intolerancesFilters).joinToString(", "), number = 1)
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
