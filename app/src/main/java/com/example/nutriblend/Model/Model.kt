package com.example.nutriblend.Model

import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nutriblend.dao.AppLocalDatabase
import java.util.concurrent.Executors

class Model private constructor(){

    enum class LoadingState {
        LOADING,
        LOADED
    }

    private val database = AppLocalDatabase.db
    private var executor = Executors.newSingleThreadExecutor()
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebaseModel = FirebaseModel()
    private val recipes: LiveData<MutableList<Recipe>>? = null
    val recipesListLoadingState: MutableLiveData<LoadingState> = MutableLiveData(LoadingState.LOADED)

    companion object {
        val instance: Model = Model()
    }
    interface GetAllRecipesListener {
        fun onComplete(recipes: List<Recipe>)
    }

    fun getAllRecipes(): LiveData<MutableList<Recipe>> {
        refreshAllRecipes()
        return recipes ?: database.RecipeDao().getAll()
    }
    fun refreshAllRecipes() {

        recipesListLoadingState.value = LoadingState.LOADING

        // 1. get last local update
        val lastUpdated: Long = Recipe.lastUpdated

        // 2. get all updated records from fs since last update locally
        firebaseModel.getAllRecipes(lastUpdated) {recipesList ->
            Log.i("TAG", "Firebase returned ${recipesList.size}, lastUpdated: $lastUpdated")

            // 3. insert new records to room
            executor.execute {
                var time = lastUpdated
                for (recipe in recipesList) {
                    database.RecipeDao().insert(recipe)

                    recipe.lastUpdated?.let {
                        if (time < it) {
                            time = recipe.lastUpdated ?: System.currentTimeMillis()
                        }
                    }
                }

                // 4. update local data
                Recipe.lastUpdated = time

                recipesListLoadingState.postValue(LoadingState.LOADED)
            }
        }
    }

    fun addRecipe(recipe: Recipe, callback: () -> Unit) {
        firebaseModel.addRecipe(recipe) {
            refreshAllRecipes()
            callback()
        }
    }

    fun signupNewUser(user: User, callback: () -> Unit) {
        Log.i("TAG", "reached user: ${user}")

        firebaseModel.signupNewUser(user) {
            callback()
        }
    }
}