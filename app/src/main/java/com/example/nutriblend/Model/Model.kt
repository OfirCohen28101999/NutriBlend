package com.example.nutriblend.Model

import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.nutriblend.dao.AppLocalDatabase
import java.util.concurrent.Executors

class Model private constructor(){
    private val database = AppLocalDatabase.db
    private var executor = Executors.newSingleThreadExecutor()
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())

    companion object {
        val instance: Model = Model()
    }
    interface GetAllRecipesListener {
        fun onComplete(recipes: List<Recipe>)
    }
    fun getAllRecipes(callback: (List<Recipe>) -> Unit) {
        executor.execute {

            Thread.sleep(5000)
            val recipes = database.RecipeDao().getAll()

            mainHandler.post {
                callback(recipes)
            }
        }
    }
    fun addRecipe(recipe: Recipe, callback: () -> Unit) {
        executor.execute{

            database.RecipeDao().insert(recipe)
            mainHandler.post{
                callback()
            }
        }
    }
}


//for (i in 0..20){
//    val recipe = Recipe("new recipe title $i",
//        "200 grams blueberries, 1 cup flour, half a cup sugar, 1 egg, 1 spoon butter, 1 cup buttermilk",
//        "mix in bowl and throw in the oven 180deg for half an hour",
//        "id: $i",
//        "imageUrl",
//        "ofirCohenUserId" )
//
//    recipes.add(recipe)
//}