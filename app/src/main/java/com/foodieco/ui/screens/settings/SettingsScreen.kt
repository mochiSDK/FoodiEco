package com.foodieco.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.FormatPaint
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    val currentContext = LocalContext.current
    var currentTheme by remember { mutableStateOf(Theme.System) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Back arrow button")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Icon(Icons.Outlined.FormatPaint, "Brush icon")
                Text("Theme", modifier = Modifier.padding(14.dp))
                Spacer(modifier = Modifier.weight(1f))
                Box {
                    TextButton(onClick = { isMenuExpanded = true }) {
                        Text(currentTheme.toString())
                        Icon(Icons.Outlined.ArrowDropDown, "Arrow drop down icon")
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false }
                    ) {
                        Theme.entries.forEach { theme ->
                            DropdownMenuItem(
                                text = { Text(theme.toString()) },
                                leadingIcon = {
                                    val icon: Pair<ImageVector, String> = when (theme) {
                                        Theme.System -> Pair(Icons.Outlined.Smartphone, "Smartphone icon")
                                        Theme.Light -> Pair(Icons.Outlined.LightMode, "Light mode icon")
                                        Theme.Dark -> Pair(Icons.Outlined.DarkMode, "Dark mode icon")
                                    }
                                    Icon(icon.first, icon.second)
                                },
                                onClick = {
                                    currentTheme = theme
                                    isMenuExpanded = false
                                    Toast.makeText(
                                        currentContext,
                                        "FoodiEco theme changed to $currentTheme",
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                                modifier = if (currentTheme == theme)
                                    Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                                else Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class Theme { Light, Dark, System }
