package com.foodieco.ui.composables

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.foodieco.data.models.SessionStatus
import com.foodieco.data.remote.OSMDataSource
import com.foodieco.ui.UserViewModel
import com.foodieco.ui.screens.favorites.FavoritesScreen
import com.foodieco.ui.screens.home.HomeScreen
import com.foodieco.ui.screens.profile.ProfileScreen
import com.foodieco.ui.screens.recipe.FavoriteRecipeViewModel
import com.foodieco.ui.screens.recipe.RecipeDetails
import com.foodieco.ui.screens.settings.SettingsScreen
import com.foodieco.ui.screens.settings.SettingsViewModel
import com.foodieco.ui.screens.settings.ThemeState
import com.foodieco.ui.screens.signin.SignInScreen
import com.foodieco.ui.screens.signup.SignUpScreen
import com.foodieco.ui.screens.stats.StatsScreen
import com.foodieco.utils.LocationService
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

sealed class NavigationRoute(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {
    data object Favorites : NavigationRoute("favorites")
    data object Home : NavigationRoute("home")
    data object Profile : NavigationRoute("profile")
    data object RecipeDetails : NavigationRoute(
        "recipes/{recipeId}",
        listOf(navArgument("recipeId") { type = NavType.StringType })
    ) {
        fun buildRoute(recipeId: String) = "recipes/$recipeId"
    }
    data object Settings : NavigationRoute("settings")
    data object SignIn : NavigationRoute("sign-in")
    data object SignUp : NavigationRoute("sign-up")
    data object Stats : NavigationRoute("stats")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel,
    themeState: ThemeState,
    locationService: LocationService,
    modifier: Modifier = Modifier
) {
    val userViewModel = koinViewModel<UserViewModel>()
    val userState by userViewModel.state.collectAsStateWithLifecycle()
    val favoriteRecipeViewModel = koinViewModel<FavoriteRecipeViewModel>()
    val favoriteRecipeState by favoriteRecipeViewModel.state.collectAsStateWithLifecycle()
    val osmDataSource = koinInject<OSMDataSource>()
    NavHost(
        navController = navController,
        startDestination = when (userState.sessionStatus) {
            SessionStatus.LoggedIn ->  NavigationRoute.Home.route
            SessionStatus.LoggedOut -> NavigationRoute.SignIn.route
        },
        modifier = modifier
    ) {
        val slideInVerticallyFromBottom = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }) 
        with(NavigationRoute.Favorites) {
            composable(
                route,
                enterTransition = { slideInVerticallyFromBottom },
                exitTransition = { fadeOut() }
            ) {
                FavoritesScreen(
                    navController,
                    favoriteRecipeState,
                    favoriteRecipeViewModel.actions::addFavorite,
                    favoriteRecipeViewModel.actions::removeFavorite
                )
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
                HomeScreen(
                    navController,
                    userState,
                    osmDataSource,
                    userViewModel::setSessionStatus,
                    favoriteRecipeState,
                    favoriteRecipeViewModel.actions::addFavorite,
                    favoriteRecipeViewModel.actions::removeFavorite
                )
            }
        }
        with(NavigationRoute.Profile) {
            composable(
                route,
                enterTransition = { slideInVerticallyFromBottom },
                exitTransition = { fadeOut() }
            ) {
                ProfileScreen(
                    navController,
                    userState,
                    locationService,
                    userViewModel::setUsername,
                    userViewModel::setLocation,
                    userViewModel::setProfilePicture
                )
            }
        }
        with(NavigationRoute.RecipeDetails) {
            composable(
                route,
                arguments,
                enterTransition = { slideInVerticallyFromBottom },
                exitTransition = { fadeOut() }
            ) { backStackEntry ->
                RecipeDetails(
                    navController,
                    backStackEntry.arguments?.getString("recipeId"),
                    favoriteRecipeState,
                    favoriteRecipeViewModel.actions::addFavorite,
                    favoriteRecipeViewModel.actions::removeFavorite,
                    osmDataSource
                )
            }
        }
        with(NavigationRoute.Settings) {
            composable(
                route,
                enterTransition = { slideInVerticallyFromBottom },
                exitTransition = { fadeOut() }
            ) {
                SettingsScreen(navController, themeState, settingsViewModel::changeTheme)
            }
        }
        with(NavigationRoute.SignIn) {
            composable(
                route,
                enterTransition = { slideInVerticallyFromBottom },
                exitTransition = { fadeOut() }
            ) {
                SignInScreen(navController, userState, userViewModel::setSessionStatus)
            }
        }
        with(NavigationRoute.SignUp) {
            composable(
                route,
                enterTransition = { slideInVerticallyFromBottom },
                exitTransition = { fadeOut() }
            ) {
                SignUpScreen(navController, userViewModel::setUsername, userViewModel::setPassword)
            }
        }
        with(NavigationRoute.Stats) {
            composable(
                route,
                enterTransition = { slideInVerticallyFromBottom },
                exitTransition = { fadeOut() }
            ) {
                StatsScreen(navController, favoriteRecipeState)
            }
        }
    }
}
