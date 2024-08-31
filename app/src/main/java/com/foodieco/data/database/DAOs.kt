package com.foodieco.data.database

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

interface FavoriteRecipeDAO {
    @Query("SELECT * FROM FavoriteRecipe")
    fun getAll(): Flow<List<FavoriteRecipe>>

    @Upsert
    suspend fun upsert(recipe: FavoriteRecipe)

    @Delete
    suspend fun delete(recipe: FavoriteRecipe)
}
