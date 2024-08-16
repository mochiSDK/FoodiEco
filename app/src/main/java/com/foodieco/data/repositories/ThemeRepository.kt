package com.foodieco.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.foodieco.data.models.Theme
import kotlinx.coroutines.flow.map

class ThemeRepository(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
    }

    val currentTheme = dataStore.data.map { preferences ->
        Theme.valueOf(preferences[THEME_KEY] ?: Theme.System.name)    // Making System the default theme.
    }

    suspend fun setTheme(theme: Theme) {
        dataStore.edit { preferences -> preferences[THEME_KEY] = theme.toString() }
    }
}
