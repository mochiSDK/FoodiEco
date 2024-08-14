package com.foodieco.ui.composables

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
        val slideInVerticallyFromBottom = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }) 
        with(NavigationRoute.Favorites) {
            composable(
                route,
                enterTransition = { slideInVerticallyFromBottom },
                exitTransition = { fadeOut() }
            ) {
                FavoritesScreen(navController)
            }
        }
        with(NavigationRoute.Home) {
            composable(
                route,
                enterTransition = {
                    slideInVertically(animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    ))
                },
                exitTransition = { slideOutVertically() }
            ) {
                HomeScreen(navController)
            }
        }
        with(NavigationRoute.Profile) {
            composable(
                route,
                enterTransition = { slideInVerticallyFromBottom },
                exitTransition = { fadeOut() }
            ) {
                ProfileScreen(navController)
            }
        }
        with(NavigationRoute.Settings) {
            composable(
                route,
                enterTransition = { slideInVerticallyFromBottom },
                exitTransition = { fadeOut() }
            ) {
                SettingsScreen(navController)
            }
        }
        // TODO: tweak animations below
        with(NavigationRoute.SignIn) {
            composable(
                route,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() }
            ) {
                SignInScreen(navController)
            }
        }
        with(NavigationRoute.SignUp) {
            composable(
                route,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() }
            ) {
                SignUpScreen(navController)
            }
        }
    }
}
