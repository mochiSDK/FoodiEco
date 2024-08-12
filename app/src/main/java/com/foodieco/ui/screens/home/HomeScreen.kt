package com.foodieco.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodieco.ui.composables.Monogram
import com.foodieco.ui.theme.FoodiEcoTheme
import kotlinx.coroutines.launch

val homeScreenPadding = 8.dp
val chipsPadding = 4.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isHomeSelected by remember { mutableStateOf(true) }
    var isFavoritesSelected by remember { mutableStateOf(false) }
    var isSettingsSelected by remember { mutableStateOf(false) }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {    // TODO: fill icons when item is selected
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
                    }
                )
                HorizontalDivider()
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
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                SearchBar(
                    placeholder = { Text("What's in the frige?") },
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    leadingIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply { if (isClosed) open() else close() }
                            }
                        }) {
                            Icon(Icons.Outlined.Menu, "Menu icon")
                        }
                    },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                            Icon(Icons.Outlined.Search, "Menu icon")
                            Spacer(modifier = Modifier.width(14.dp))
                            IconButton(onClick = { /*TODO*/ }) {
                                Monogram(text = "A", size = 48.dp, modifier = Modifier.clip(CircleShape))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(homeScreenPadding)
                ) {

                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier
                .padding(innerPadding)
                .padding(homeScreenPadding)) {
                Row {   // TODO: add horizontal scroll
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FoodiEcoTheme {
        HomeScreen()
    }
}
