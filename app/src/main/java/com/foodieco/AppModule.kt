package com.foodieco

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.foodieco.data.repositories.ThemeRepository
import com.foodieco.ui.screens.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("theme")

val appModule = module {
    single { get<Context>().dataStore }

    single { ThemeRepository(get()) }

    viewModel { SettingsViewModel(get()) }
}
