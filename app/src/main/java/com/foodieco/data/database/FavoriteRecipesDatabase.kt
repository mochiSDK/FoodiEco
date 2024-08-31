package com.foodieco.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteRecipe::class], version = 1)
abstract class FavoriteRecipesDatabase : RoomDatabase() {
    abstract fun recipeDAO(): FavoriteRecipeDAO
}
