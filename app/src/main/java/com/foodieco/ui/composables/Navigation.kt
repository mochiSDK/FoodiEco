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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.foodieco.UserViewModel
import com.foodieco.data.models.SessionStatus
import com.foodieco.ui.screens.favorites.FavoritesScreen
import com.foodieco.ui.screens.home.HomeScreen
import com.foodieco.ui.screens.profile.ProfileScreen
import com.foodieco.ui.screens.settings.SettingsScreen
import com.foodieco.ui.screens.settings.SettingsViewModel
import com.foodieco.ui.screens.settings.ThemeState
import com.foodieco.ui.screens.signin.SignInScreen
import com.foodieco.ui.screens.signup.SignUpScreen
import org.koin.androidx.compose.koinViewModel

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
    settingsViewModel: SettingsViewModel,
    themeState: ThemeState,
    modifier: Modifier = Modifier
) {
    val userViewModel = koinViewModel<UserViewModel>()
    val userState by userViewModel.state.collectAsStateWithLifecycle()
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
    }
}
