package com.example.nutriblend.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CalorieNinjasApi {
    @Headers("X-Api-Key: " + "pAFUWSnzlFv50UF9fsXGOQ==KN5hIBKAiPsbnpA8")
    @GET("v1/nutrition")
    fun getNutritionalInfoList(@Query("query") ingredientsList: String) : Call<NutritionalInfoList>
}