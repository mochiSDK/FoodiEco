package com.foodieco.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.foodieco.ui.screens.favorites.FavoritesScreen
import com.foodieco.ui.screens.home.HomeScreen
import com.foodieco.ui.screens.profile.ProfileScreen
import com.foodieco.ui.screens.settings.SettingsScreen
import com.foodieco.ui.screens.signin.SignInScreen
import com.foodieco.ui.screens.signup.SignUpScreen

sealed class NavigationRoute(val route: String) {
    data object Favorites : NavigationRoute("favorites")
    data object Home : NavigationRoute("home")
    data object Profile : NavigationRoute("profile")
    data object Settings : NavigationRoute("settings")
    data object SignIn : NavigationRoute("sign-in")
    data object SignUp : NavigationRoute("sign-up")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Home.route,
        modifier = modifier
    ) {
        with(NavigationRoute.Favorites) {
            composable(route) { FavoritesScreen(navController) }
        }
        with(NavigationRoute.Home) {
            composable(route) { HomeScreen(navController) }
        }
        with(NavigationRoute.Profile) {
            composable(route) { ProfileScreen(navController) }
        }
        with(NavigationRoute.Settings) {
            composable(route) { SettingsScreen(navController) }
        }
        with(NavigationRoute.SignIn) {
            composable(route) { SignInScreen(navController) }
        }
        with(NavigationRoute.SignUp) {
            composable(route) { SignUpScreen(navController) }
        }
    }
}
