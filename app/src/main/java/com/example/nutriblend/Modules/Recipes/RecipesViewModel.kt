package com.example.nutriblend.Modules.Recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.Model.User

class RecipesViewModel: ViewModel() {
    var recipes: LiveData<MutableList<Recipe>>? = null
    var users: LiveData<MutableList<User>>? = null
}