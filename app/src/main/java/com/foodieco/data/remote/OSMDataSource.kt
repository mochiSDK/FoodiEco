package com.foodieco.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OSMApiResult(
    @SerialName("results")
    val results: List<OSMRecipe>
)

@Serializable
data class OSMRecipe(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("image")
    val image: String,
    @SerialName("servings")
    val servings: Int,
    @SerialName("readyInMinutes")
    val readyInMinutes: Int,
    @SerialName("cuisines")
    val cuisines: List<String>,
    @SerialName("extendedIngredients")
    val ingredients: List<OSMIngredient>,
    @SerialName("missedIngredients")
    val missedIngredients: List<OSMIngredient>,
    @SerialName("dishTypes")
    val types: List<String>,
    @SerialName("spoonacularScore")
    val score: Double,
    @SerialName("analyzedInstructions")
    val instructions: List<OSMInstruction>
)

@Serializable
data class OSMIngredient(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("amount")
    val amount: Double,
    @SerialName("unit")
    val unit: String
)

@Serializable
data class OSMInstruction(
    @SerialName("steps")
    val steps: List<OSMStep>?
)

@Serializable
data class OSMStep(
    @SerialName("number")
    val number: Int,
    @SerialName("step")
    val step: String,
)

class OSMDataSource(private val httpClient: HttpClient) {
    private val baseUrl = "https://api.spoonacular.com"

    suspend fun searchRecipes(ingredients: String, max: Int = 10): List<OSMRecipe> {
        return httpClient.get("$baseUrl/recipes/complexSearch") {
            url {
                parameters.append("includeIngredients", ingredients)
                parameters.append("addRecipeInformation", "true")
                parameters.append("addRecipeInstructions", "true")
                parameters.append("ignorePantry", "true")
                parameters.append("fillIngredients", "true")
                parameters.append("sort", "min-missing-ingredients")
                parameters.append("number", max.toString())
            }
        }.body<OSMApiResult>().results
    }
}
