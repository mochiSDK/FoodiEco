package com.foodieco

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.foodieco.data.database.FavoriteRecipesDatabase
import com.foodieco.data.remote.OSMDataSource
import com.foodieco.data.repositories.FavoriteRecipeRepository
import com.foodieco.data.repositories.ThemeRepository
import com.foodieco.data.repositories.UserRepository
import com.foodieco.ui.screens.recipe.FavoriteRecipeViewModel
import com.foodieco.ui.screens.settings.SettingsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("theme")

val appModule = module {
    single { get<Context>().dataStore }

    single { ThemeRepository(get()) }

    viewModel { SettingsViewModel(get()) }

    single { UserRepository(get()) }

    viewModel { UserViewModel(get()) }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true } )
            }
            defaultRequest {
                header("x-api-key", BuildConfig.API_KEY)
            }
        }
    }

    single { OSMDataSource(get()) }

    single {
        Room.databaseBuilder(
            get(),
            FavoriteRecipesDatabase::class.java,
            "favoriteRecipesDb"
        ).build()
    }

    single { FavoriteRecipeRepository(get<FavoriteRecipesDatabase>().recipeDAO()) }

    viewModel { FavoriteRecipeViewModel(get()) }
}
