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

@Serializable
data class OSMRecipeInfo(
    @SerialName("id")
    val id: Int,
    @SerialName("servings")
    val servings: Int,
    @SerialName("readyInMinutes")
    val readyInMinutes: Int,
    @SerialName("cuisines")
    val cuisines: List<String>,
    @SerialName("extendedIngredients")
    val ingredients: List<OSMIngredient>,
    @SerialName("instructions")
    val instructions: String,
    @SerialName("summary")
    val summary: String
)

@Serializable
data class OSMIngredient(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("measures")
    val measures: OSMMeasure
)

@Serializable
data class OSMMeasure(
    @SerialName("metric")
    val metric: OSMUnit
)

@Serializable
data class OSMUnit(
    @SerialName("amount")
    val amount: Int,
    @SerialName("unitShort")
    val unit: String
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

    suspend fun getRecipeInfo(id: Int): OSMRecipeInfo {
        return httpClient.get("$baseUrl/recipes/$id/information") {
            url {
                parameters.append("includeNutrition", "false")
                parameters.append("addWinePairing", "false")
                parameters.append("addTasteData", "false")
            }
        }.body()
    }
}
