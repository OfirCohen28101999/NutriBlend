package com.example.nutriblend.api

import com.example.nutriblend.Model.Recipe
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.calorieninjas.com/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object ApiClient {
    val apiService: CalorieNinjasApi by lazy {
        RetrofitClient.retrofit.create(CalorieNinjasApi::class.java)
    }

    fun createRecipeWithNutritionalInfo(nutritionalInfoList: NutritionalInfoList,
                                        recipeId: String,
                                        title: String,
                                        ingredients: String,
                                        preparationSteps: String,
                                        imageUrl: String?,
                                        creatingUserId: String): Recipe {
        val summedUpNutritionalInfo: NutritionalInfo = NutritionalInfo(0.00,
            0.00,
            0.00,
            0.00,
            "recipeTitle",
            0.00,
            0.00,
            0.00,
            0.00,
            0.00,
            0.00,
            0.00)

        nutritionalInfoList?.let {
            for (nutritionalInfo in nutritionalInfoList.items){
                summedUpNutritionalInfo.sugar_g += nutritionalInfo.sugar_g
                summedUpNutritionalInfo.fiber_g += nutritionalInfo.fiber_g
                summedUpNutritionalInfo.serving_size_g += nutritionalInfo.serving_size_g
                summedUpNutritionalInfo.sodium_mg += nutritionalInfo.sodium_mg
                summedUpNutritionalInfo.potassium_mg += nutritionalInfo.potassium_mg
                summedUpNutritionalInfo.fat_saturated_g += nutritionalInfo.fat_saturated_g
                summedUpNutritionalInfo.fat_total_g += nutritionalInfo.fat_total_g
                summedUpNutritionalInfo.calories += nutritionalInfo.calories
                summedUpNutritionalInfo.cholesterol_mg += nutritionalInfo.cholesterol_mg
                summedUpNutritionalInfo.protein_g += nutritionalInfo.protein_g
                summedUpNutritionalInfo.carbohydrates_total_g += nutritionalInfo.carbohydrates_total_g
            }
        }

        return Recipe(
            id = recipeId,
            title = title,
            ingredients = ingredients,
            preparationSteps = preparationSteps,
            imageUrl = imageUrl,
            creatingUserId = creatingUserId,
            calories = summedUpNutritionalInfo.calories,
            sugar_g = summedUpNutritionalInfo.sugar_g,
            fiber_g = summedUpNutritionalInfo.fiber_g,
            sodium_mg = summedUpNutritionalInfo.sodium_mg,
            potassium_mg = summedUpNutritionalInfo.potassium_mg,
            fat_saturated_g = summedUpNutritionalInfo.fat_saturated_g,
            fat_total_g = summedUpNutritionalInfo.fat_total_g,
            cholesterol_mg = summedUpNutritionalInfo.cholesterol_mg,
            protein_g = summedUpNutritionalInfo.protein_g,
            carbohydrates_total_g = summedUpNutritionalInfo.carbohydrates_total_g
        )
    }
}
