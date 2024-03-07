package com.example.nutriblend.Model

import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import com.example.nutriblend.dao.AppLocalDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        firebaseModel.getAllRecipes(callback)
    }

    fun addRecipe(recipe: Recipe, callback: () -> Unit) {
        firebaseModel.addRecipe(recipe, callback)
    }
}