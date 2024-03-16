package com.example.nutriblend.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutriblend.Model.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM Recipe")
    fun getAll(): LiveData<MutableList<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg recipes: Recipe)

    @Delete
    fun delete(recipe: Recipe)

    @Query("SELECT * FROM RECIPE WHERE id=:id")
    fun getRecipeById(id: String): LiveData<Recipe?>

    @Query("SELECT * FROM RECIPE WHERE creatingUserId=:userId")
    fun getRecipesByCreatingUserId(userId: String): LiveData<MutableList<Recipe>>
}