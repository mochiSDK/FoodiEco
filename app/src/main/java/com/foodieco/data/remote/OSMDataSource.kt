package com.foodieco.data.remote

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OSMApiResult(
    @SerialName("results")
    val results: List<OSMRecipe>
)

@Serializable
data class OSMRandomRecipeApiResult(
    @SerialName("recipes")
    val recipes: List<OSMRecipe>
)

@Serializable
data class OSMRecipe(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("image")
    val image: String? = null,
    @SerialName("cuisines")
    val cuisines: List<String>,
    @SerialName("diets")
    val diets: List<String>
)

@Serializable
data class OSMRecipeDetails(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("image")
    val image: String? = null,
    @SerialName("servings")
    val servings: Int,
    @SerialName("readyInMinutes")
    val readyInMinutes: Int,
    @SerialName("extendedIngredients")
    val ingredients: List<OSMIngredient>,
    @SerialName("cuisines")
    val cuisines: List<String>,
    @SerialName("dishTypes")
    val types: List<String>,
    @SerialName("healthScore")
    val score: Double,
    @SerialName("dairyFree")
    val isDairyFree: Boolean,
    @SerialName("glutenFree")
    val isGlutenFree: Boolean,
    @SerialName("vegan")
    val isVegan: Boolean,
    @SerialName("nutrition")
    val nutrition: OSMNutrition,
    @SerialName("analyzedInstructions")
    val instructions: List<OSMInstruction>,
    @SerialName("creditsText")
    val credits: String
)

@Serializable
data class OSMIngredient(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("measures")
    val measures: OSMMeasures
)

@Serializable
data class OSMMeasures(
    @SerialName("metric")
    val metric: OSMMeasureDetails,
    @SerialName("us")
    val us: OSMMeasureDetails
)

@Serializable
data class OSMMeasureDetails(
    @SerialName("amount")
    val amount: Double,
    @SerialName("unitShort")
    val unit: String
)

@Serializable
data class OSMNutrition(
    @SerialName("caloricBreakdown")
    val caloricBreakdown: OSMCaloricBreakdown
)

@Serializable
data class OSMCaloricBreakdown(
    @SerialName("percentProtein")
    val percentProtein: Double,
    @SerialName("percentFat")
    val percentFat: Double,
    @SerialName("percentCarbs")
    val percentCarbs: Double
)

@Serializable
data class OSMInstruction(
    @SerialName("name")
    val name: String,
    @SerialName("steps")
    val steps: List<OSMStep>
)

@Serializable
data class OSMStep(
    @SerialName("number")
    val number: Int,
    @SerialName("step")
    val step: String
)

class OSMDataSource(private val httpClient: HttpClient) {
    private val baseUrl = "https://api.spoonacular.com"

    suspend fun searchRecipes(
        ingredients: String,
        cuisines: String = "",
        diets: String = "",
        intolerances: String = "",
        max: Int = 10
    ): List<OSMRecipe>? {
        return httpClient.get("$baseUrl/recipes/complexSearch") {
            url {
                parameters.append("includeIngredients", ingredients)
                parameters.append("addRecipeInformation", "true")
                parameters.append("ignorePantry", "true")
                parameters.append("fillIngredients", "true")
                parameters.append("sort", "min-missing-ingredients")
                parameters.append("number", max.toString())
                if (cuisines.isNotEmpty()) {
                    parameters.append("cuisine", cuisines)
                }
                if (diets.isNotEmpty()) {
                    parameters.append("diet", diets)
                }
                if (intolerances.isNotEmpty()) {
                    parameters.append("intolerances", intolerances)
                }
            }
        }.okStatusOrNull { response -> response.body<OSMApiResult>().results }
    }

    suspend fun searchRecipeById(id: Int): OSMRecipeDetails? {
        return httpClient.get("$baseUrl/recipes/$id/information") {
            url {
                parameters.append("includeNutrition", "true")
            }
        }.okStatusOrNull { response -> response.body() }
    }

    suspend fun getRandomRecipes(tags: String = "", number: Int = 10
    ): List<OSMRecipe>? {
        return httpClient.get("$baseUrl/recipes/random") {
            url {
                if (tags.isNotEmpty()) {
                    parameters.append("include-tags", tags.toLowerCase(Locale.current))
                }
                parameters.append("number", number.toString())
            }
        }.okStatusOrNull { response -> response.body<OSMRandomRecipeApiResult>().recipes }
    }

    /**
     * Returns null if the http status is not OK (200),
     * the returnBlock (called on the original HttpResponse object) lambda type otherwise.
     */
    private inline fun <T> HttpResponse.okStatusOrNull(returnBlock: (HttpResponse) -> T): T? {
        return when {
            this.status == HttpStatusCode.OK -> returnBlock(this)
            else -> null
        }
    }
}
