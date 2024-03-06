package com.example.nutriblend.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.base.MyApplication

@Database(entities = [Recipe::class], version = 2)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun RecipeDao(): RecipeDao
}

object AppLocalDatabase {

    val db: AppLocalDbRepository by lazy {

        val context = MyApplication.Globals.appContext ?: throw IllegalStateException("app context not available")

        Room.databaseBuilder(context, AppLocalDbRepository::class.java, "dbFileName.db")
            .fallbackToDestructiveMigration()
            .build()
    }

}