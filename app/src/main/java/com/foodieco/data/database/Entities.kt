package com.foodieco.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteRecipe(
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val image: String?,
    @ColumnInfo val cuisines: String
)
