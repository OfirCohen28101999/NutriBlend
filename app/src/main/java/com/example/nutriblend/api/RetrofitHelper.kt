package com.example.nutriblend.api

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

    fun sumUp(nutritionalInfoList: NutritionalInfoList): NutritionalInfo {
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

        return summedUpNutritionalInfo
    }
}
