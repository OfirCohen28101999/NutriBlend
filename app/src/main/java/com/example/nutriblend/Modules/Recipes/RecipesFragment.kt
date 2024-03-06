package com.example.nutriblend.Modules.Recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.Modules.Recipes.Adapter.RecipesRecyclerAdapter

import com.example.nutriblend.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {
    var recipesRecyclerView: RecyclerView? = null
    private var recipes: List<Recipe>? = null
    var adapter: RecipesRecyclerAdapter? = null
    var progressBar: ProgressBar? = null

    private var _binding:  FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root // inflater.inflate(R.layout.fragment_recipes, container, false)
        progressBar = binding.progressBar // view.findViewById(R.id.progressBar)
        progressBar?.visibility = View.VISIBLE

        Model.instance.getAllRecipes { recipes ->
            this.recipes = recipes
            adapter?.recipes = recipes
            adapter?.notifyDataSetChanged()

            progressBar?.visibility = View.GONE
        }

        recipesRecyclerView = binding.rvRecipesFragmentList // view.findViewById(R.id.rvRecipesFragmentList)
        recipesRecyclerView?.setHasFixedSize(true)

        // set layout manager
        recipesRecyclerView?.layoutManager = LinearLayoutManager(context)

        // set the adapter
        recipesRecyclerView?.adapter = RecipesRecyclerAdapter(recipes)

        adapter = RecipesRecyclerAdapter(recipes)
        adapter?.listener = object : RecipesRecyclerViewActivity.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.i("TAG","RecipesRecyclerAdapter: POSITION CLICKED ${position}")
                val recipe = recipes?.get(position)
                recipe?.let {
                    val action = RecipesFragmentDirections.actionRecipesFragmentToBlueFragment().setTitleArgBlueFragment(it.title)
                    Navigation.findNavController(view).navigate(action)
                }
            }

            override fun onRecipeClicked(recipe: Recipe?) {
                Log.i("TAG","RecipesRecyclerAdapter: RECIPE ${recipe}")            }
        }

        recipesRecyclerView?.adapter = adapter

        val addRecipeBtn: ImageButton = binding.iBtnRecipesFragmentAddRecipe // view.findViewById(R.id.iBtnRecipesFragmentAddRecipe)
        val action = Navigation.createNavigateOnClickListener(RecipesFragmentDirections.actionGlobalAddRecipeFragment())
        addRecipeBtn.setOnClickListener(action)

        return view
    }

    override fun onResume() {
        super.onResume()

        progressBar?.visibility = View.VISIBLE

        Model.instance.getAllRecipes { recipes ->
            this.recipes = recipes
            adapter?.recipes = recipes
            adapter?.notifyDataSetChanged()

            progressBar?.visibility = View.GONE

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}