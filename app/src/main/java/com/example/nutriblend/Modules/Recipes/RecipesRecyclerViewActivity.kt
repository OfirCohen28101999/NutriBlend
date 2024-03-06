package com.example.nutriblend.Modules.Recipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.Modules.Recipes.Adapter.RecipesRecyclerAdapter
import com.example.nutriblend.R

// not in use oops

class RecipesRecyclerViewActivity : AppCompatActivity() {
    var recipesRecyclerView: RecyclerView? = null
    private var recipes: List<Recipe>? = null
    var adapter: RecipesRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_recycler_view)

        adapter = RecipesRecyclerAdapter(recipes)

        Model.instance.getAllRecipes { recipes ->
            this.recipes = recipes
            adapter?.recipes = recipes
            adapter?.notifyDataSetChanged()
        }

        recipesRecyclerView = findViewById(R.id.rvRecipesRecyclerList)
        recipesRecyclerView?.setHasFixedSize(true)

        // set layout manager
        recipesRecyclerView?.layoutManager = LinearLayoutManager(this)

        adapter?.listener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.i("TAG","RecipesRecyclerAdapter: POSITION CLICKED ${position}")
            }

            override fun onRecipeClicked(recipe: Recipe?) {
                Log.i("TAG","RecipesRecyclerAdapter: RECIPE ${recipe}")            }
        }

        recipesRecyclerView?.adapter = adapter
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int) // Recipe
        fun onRecipeClicked(recipe: Recipe?)
    }

    override fun onResume() {
        super.onResume()

        Model.instance.getAllRecipes { recipes ->
            this.recipes = recipes
            adapter?.recipes = recipes
            adapter?.notifyDataSetChanged()
        }

    }
}