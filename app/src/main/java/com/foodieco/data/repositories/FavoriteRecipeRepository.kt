package com.foodieco.data.repositories

import com.foodieco.data.database.FavoriteRecipe
import com.foodieco.data.database.FavoriteRecipeDAO

class FavoriteRecipeRepository(private val favoriteRecipeDAO: FavoriteRecipeDAO) {
    val recipes = favoriteRecipeDAO.getAll()

    suspend fun upsert(recipe: FavoriteRecipe) = favoriteRecipeDAO.upsert(recipe)

    suspend fun delete(recipe: FavoriteRecipe) = favoriteRecipeDAO.delete(recipe)
}
