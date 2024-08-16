package com.foodieco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.foodieco.data.models.Theme
import com.foodieco.ui.composables.NavGraph
import com.foodieco.ui.screens.settings.SettingsViewModel
import com.foodieco.ui.theme.FoodiEcoTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel = koinViewModel<SettingsViewModel>()
            val themeState by settingsViewModel.state.collectAsStateWithLifecycle()
            FoodiEcoTheme(
                darkTheme = when (themeState.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ) {
                Box {
                    val navController = rememberNavController()
                    NavGraph(navController = navController, settingsViewModel, themeState)
                }
            }
        }
    }
}
