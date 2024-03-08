package com.example.nutriblend.Model

import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import com.example.nutriblend.dao.AppLocalDatabase
import java.util.concurrent.Executors

class Model private constructor(){
    // TODO: local cache still needs to be implemented with room
    private val database = AppLocalDatabase.db
    private var executor = Executors.newSingleThreadExecutor()
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebaseModel = FirebaseModel()

    companion object {
        val instance: Model = Model()
    }
    interface GetAllRecipesListener {
        fun onComplete(recipes: List<Recipe>)
    }

    fun getAllRecipes(callback: (List<Recipe>) -> Unit) {
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

                // 5. return complete list from room - we fetch data only from room we will fetch updates from fs to room
                val recipes = database.RecipeDao().getAll()

                mainHandler.post {
                    callback(recipes)
                }
            }
        }
    }

    fun addRecipe(recipe: Recipe, callback: () -> Unit) {
        firebaseModel.addRecipe(recipe, callback)
    }
}



/// https://moodle.colman.ac.il/mod/zoom/loadrecording.php?id=18636&recordingid=21129 01:10:00 40825814