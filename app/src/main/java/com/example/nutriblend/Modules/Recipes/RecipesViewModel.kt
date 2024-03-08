package com.example.nutriblend.Modules.Recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nutriblend.Model.Recipe

class RecipesViewModel: ViewModel() {
    var recipes: LiveData<MutableList<Recipe>>? = null
}