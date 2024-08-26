package com.foodieco.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OSMRecipe(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("image")
    val image: String,
)

class OSMDataSource(private val httpClient: HttpClient) {
    private val baseUrl = "https://api.spoonacular.com"

    suspend fun searchRecipes(ingredient: String, max: Int): List<OSMRecipe> {
        return httpClient.get("$baseUrl/recipes/findByIngredients") {
            url {
                parameters.append("ingredients", ingredient)
                parameters.append("number", max.toString())
            }
        }.body()
    }
}
