package com.foodieco.ui.screens.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.NoFood
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.RoomService
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.foodieco.UserState
import com.foodieco.data.models.SessionStatus
import com.foodieco.data.remote.OSMDataSource
import com.foodieco.data.remote.OSMRecipe
import com.foodieco.ui.composables.FoodCard
import com.foodieco.ui.composables.Monogram
import com.foodieco.ui.composables.NavigationRoute
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

val homeScreenPadding = 8.dp
val chipsPadding = 4.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    userState: UserState,
    logout: (SessionStatus) -> Unit
) {
    var searchBarQuery by rememberSaveable { mutableStateOf("") }
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

    fun isOnline(): Boolean {
        val connectivityManager = ctx.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
                || capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true

    }

    fun openWirelessSettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (intent.resolveActivity(ctx.applicationContext.packageManager) != null) {
            ctx.applicationContext.startActivity(intent)
        }
    }

    val osmDataSource = koinInject<OSMDataSource>()
    var recipes by rememberSaveable { mutableStateOf<List<OSMRecipe>?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }
    fun searchRecipe(ingredients: String) = coroutineScope.launch {
        if (isOnline()) {
            val result = osmDataSource.searchRecipes(ingredients)    // TODO: put appropriate max number
            recipes = result.ifEmpty { null }
        } else {
            val snackbarResult = snackBarHostState.showSnackbar(
                message = "No Internet connection",
                actionLabel = "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                openWirelessSettings()
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
                    badge = { /*TODO*/ },
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
        Scaffold(
            topBar = {
                SearchBar(
                    placeholder = { Text("What's in the fridge?") },
                    query = searchBarQuery,
                    onQueryChange = { searchBarQuery = it },
                    onSearch = ::searchRecipe,
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
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { innerPadding ->
            LazyColumn(modifier = Modifier
                .padding(innerPadding)
                .padding(homeScreenPadding)
            ) {
                item {
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        FilterChip(
                            selected = false,
                            onClick = { /*TODO*/ },
                            label = { Text("Cuisine") },
                            leadingIcon = { Icon(Icons.Outlined.RoomService, "Cuisine icon") },
                            trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, "Arrow drop down icon") },
                            modifier = Modifier.padding(chipsPadding)
                        )
                        FilterChip(
                            selected = false,
                            onClick = { /*TODO*/ },
                            label = { Text("Diet") },
                            leadingIcon = { Icon(Icons.Outlined.Restaurant, "Diet icon") },
                            trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, "Arrow drop down icon") },
                            modifier = Modifier.padding(chipsPadding)
                        )
                        FilterChip(
                            selected = false,
                            onClick = { /*TODO*/ },
                            label = { Text("Intolerance") },
                            leadingIcon = { Icon(Icons.Outlined.NoFood, "Intolerance icon") },
                            trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, "Arrow drop down icon") },
                            modifier = Modifier.padding(chipsPadding)
                        )
                    }
                }
                recipes?.let {
                    items(it) { recipe ->
                        FoodCard(
                            title = recipe.title,
                            subtext = recipe.cuisines.joinToString(", "),
                            image = recipe.image,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
