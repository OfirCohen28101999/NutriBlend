package com.example.nutriblend.Model

import android.util.Log
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
    private val firebaseModel = FirebaseModel()
    val recipesListLoadingState: MutableLiveData<LoadingState> = MutableLiveData(LoadingState.LOADED)
    val userProfileLoadingState: MutableLiveData<LoadingState> = MutableLiveData(LoadingState.LOADED)


    companion object {
        val instance: Model = Model()
    }

    fun getAllRecipes(): LiveData<MutableList<Recipe>> {
        refreshAllRecipes()
        return database.RecipeDao().getAll()
    }

    fun getAllUsers(): LiveData<MutableList<User>> {
        refreshAllUserProfiles()
        return database.UserDao().getAll()
    }

    fun getMyRecipes(userId: String): LiveData<MutableList<Recipe>> {
        refreshAllRecipes()
        return database.RecipeDao().getRecipesByCreatingUserId(userId)
    }

    fun getRecipeById(recipeId: String): LiveData<Recipe?> {
        return database.RecipeDao().getRecipeById(recipeId)
    }
    fun refreshAllRecipes() {
        recipesListLoadingState.value = LoadingState.LOADING
        val lastUpdated: Long = Recipe.lastUpdated
        firebaseModel.getAllRecipes(lastUpdated) {recipesList ->
            Log.i("TAG", "Firebase returned ${recipesList.size} recipes, lastUpdated: $lastUpdated")

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

                Recipe.lastUpdated = time

                recipesListLoadingState.postValue(LoadingState.LOADED)
            }
        }
    }

    fun refreshAllUserProfiles() {
        userProfileLoadingState.value = LoadingState.LOADING
        val lastUpdated: Long = User.lastUpdated
        firebaseModel.getAllUserProfiles(lastUpdated) {userProfilesList ->
            Log.i("TAG", "Firebase returned ${userProfilesList.size} user profiles, lastUpdated: $lastUpdated")

            executor.execute {
                var time = lastUpdated
                for (userProfile in userProfilesList) {
                    database.UserDao().insert(userProfile)

                    userProfile.lastUpdated?.let {
                        if (time < it) {
                            time = userProfile.lastUpdated ?: System.currentTimeMillis()
                        }
                    }
                }

                User.lastUpdated = time

                userProfileLoadingState.postValue(LoadingState.LOADED)
            }
        }
    }

    fun addRecipe(recipe: Recipe, callback: () -> Unit) {
        firebaseModel.addRecipe(recipe) {
            refreshAllRecipes()
            callback()
        }
    }

    fun updateRecipe(recipe: Recipe, callback: () -> Unit) {
        firebaseModel.updateRecipe(recipe) {
                recipesListLoadingState.value = LoadingState.LOADING

                executor.execute {
                    database.RecipeDao().insert(recipe)
                    recipesListLoadingState.postValue(LoadingState.LOADED)
                }

                refreshAllRecipes()
                callback()
            }
        }
    fun deleteRecipe(recipe: Recipe, callback: () -> Unit) {
        firebaseModel.deleteRecipe(recipe.id) {

            recipesListLoadingState.value = LoadingState.LOADING

            executor.execute {
                database.RecipeDao().delete(recipe)
                recipesListLoadingState.postValue(LoadingState.LOADED)
            }

            refreshAllRecipes()
            callback()
        }
    }

    fun signupNewUser(user: User, callback: () -> Unit) {
        userProfileLoadingState.postValue(LoadingState.LOADING)

        firebaseModel.signupNewUser(user) {
            refreshAllUserProfiles()
            callback()
        }
    }

    fun getUserProfileById(userId: String): LiveData<User?> {
        return database.UserDao().getUserById(userId)
    }

    fun updateUserProfile(user: User, callback: () -> Unit) {
        firebaseModel.updateUserProfile(user) {
            refreshAllUserProfiles()
            callback()
        }
    }
}