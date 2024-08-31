package com.foodieco.ui.screens.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodieco.data.database.FavoriteRecipe
import com.foodieco.data.repositories.FavoriteRecipeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoriteRecipeState(val recipes: List<FavoriteRecipe>)

interface FavoriteRecipeActions {
    fun addFavorite(recipe: FavoriteRecipe): Job
    fun removeFavorite(recipe: FavoriteRecipe): Job
}

class FavoriteRecipeViewModel(private val repository: FavoriteRecipeRepository) : ViewModel() {
    val state = repository.recipes.map { FavoriteRecipeState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = FavoriteRecipeState(emptyList())
    )

    val actions = object : FavoriteRecipeActions {
        override fun addFavorite(recipe: FavoriteRecipe) = viewModelScope.launch {
            repository.upsert(recipe)
        }

        override fun removeFavorite(recipe: FavoriteRecipe) = viewModelScope.launch {
            repository.delete(recipe)
        }
    }
}
