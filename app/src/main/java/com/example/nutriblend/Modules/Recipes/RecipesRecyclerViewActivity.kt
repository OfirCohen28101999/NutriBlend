package com.example.nutriblend.Modules.Recipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.Modules.Recipes.Adapter.RecipesRecyclerAdapter
import com.example.nutriblend.databinding.ActivityRecipesRecyclerViewBinding

// not in use oops, used to demonstrate viewBinding

class RecipesRecyclerViewActivity : AppCompatActivity() {
    var recipesRecyclerView: RecyclerView? = null
    private var recipes: List<Recipe>? = null
    var adapter: RecipesRecyclerAdapter? = null

    private lateinit var binding: ActivityRecipesRecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecipesRecyclerViewBinding.inflate(layoutInflater)
        // setContentView(R.layout.activity_recipes_recycler_view) // not used with binding
        setContentView(binding.root)

        adapter = RecipesRecyclerAdapter(recipes)

//        Model.instance.refreshAllRecipes { recipes ->
//            this.recipes = recipes
//            adapter?.recipes = recipes
//            adapter?.notifyDataSetChanged()
//        }

        recipesRecyclerView =  binding.rvRecipesRecyclerList // findViewById(R.id.rvRecipesRecyclerList)
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

//        Model.instance.refreshAllRecipes { recipes ->
//            this.recipes = recipes
//            adapter?.recipes = recipes
//            adapter?.notifyDataSetChanged()
//        }

    }
}