package com.foodieco.ui.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.foodieco.data.models.SessionStatus
import com.foodieco.ui.screens.recipe.FavoriteRecipeState

@Composable
fun HomeNavigationDrawer(
    drawerState: DrawerState,
    toggleDrawer: () -> Unit,
    favoriteRecipeState: FavoriteRecipeState,
    logout: (SessionStatus) -> Unit,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    var isHomeSelected by remember { mutableStateOf(true) }
    var isFavoritesSelected by remember { mutableStateOf(false) }
    var isSettingsSelected by remember { mutableStateOf(false) }
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
        content()
    }
}
