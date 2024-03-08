package com.example.nutriblend.api

data class NutritionalInfo(
    var sugar_g: Double,
    var fiber_g: Double,
    var serving_size_g: Double,
    var sodium_mg: Double,
    val name: String,
    var potassium_mg: Double,
    var fat_saturated_g: Double,
    var fat_total_g: Double,
    var calories: Double,
    var cholesterol_mg: Double,
    var protein_g: Double,
    var carbohydrates_total_g: Double)

data class NutritionalInfoList(
    var items: List<NutritionalInfo>
)
