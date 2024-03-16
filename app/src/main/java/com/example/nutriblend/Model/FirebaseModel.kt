package com.example.nutriblend.Model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.ktx.Firebase
class FirebaseModel {
    val db = Firebase.firestore

    companion object {
        const val RECIPES_COLLECTION_PATH: String = "recipes"
        const val USERS_COLLECTION_PATH: String = "users"
    }
    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {  })
//            setLocalCacheSettings(persistentCacheSettings {  })
        }

        db.firestoreSettings = settings
    }

    fun getAllRecipes(since: Long, callback: (List<Recipe>) -> Unit) {
        db.collection(RECIPES_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo(Recipe.LAST_UPDATED_KEY, Timestamp(since, 0))
            .get().addOnCompleteListener{
            when (it.isSuccessful) {
                true -> {
                    val recipes: MutableList<Recipe> = mutableListOf()
                    for(json in it.result) {
                        val recipe  = Recipe.fromJson(json)
                        recipes.add(recipe)
                    }
                    callback(recipes)
                }
                false -> callback(listOf())
            }
        }
    }

    fun getAllUserProfiles(since: Long, callback: (List<User>) -> Unit) {
        db.collection(USERS_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo(User.LAST_UPDATED_KEY, Timestamp(since, 0))
            .get()
            .addOnCompleteListener{
                when (it.isSuccessful) {
                    true -> {
                        val users: MutableList<User> = mutableListOf()
                        for(json in it.result) {
                            val user  = User.fromJson(json)
                            users.add(user)
                        }
                        callback(users)
                    }
                    false -> callback(listOf())
                }
            }
    }

    fun addRecipe(recipe: Recipe, callback: () -> Unit) {
        db.collection(RECIPES_COLLECTION_PATH)
            .add(recipe.json)
            .addOnSuccessListener { documentReference ->
                callback()
            }
    }

    fun updateRecipe(recipe: Recipe, callback: () -> Unit) {
        db.collection(RECIPES_COLLECTION_PATH)
            .document(recipe.id)
            .set(recipe.json,SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                callback()
            }
    }

    fun deleteRecipe(recipeId: String, callback: () -> Unit) {
        db.collection(RECIPES_COLLECTION_PATH)
            .document(recipeId)
            .delete()
            .addOnSuccessListener { documentReference ->
                callback()
            }

    }

    fun signupNewUser(user: User, callback: () -> Unit) {
        db.collection(USERS_COLLECTION_PATH)
            .document(user.id)
            .set(user.json)
            .addOnSuccessListener { documentReference ->
                callback()
            }
    }

    fun updateUserProfile(user: User, callback: () -> Unit) {
        db.collection(USERS_COLLECTION_PATH)
            .document(user.id)
            .set(user.json,SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                callback()
            }
    }
}