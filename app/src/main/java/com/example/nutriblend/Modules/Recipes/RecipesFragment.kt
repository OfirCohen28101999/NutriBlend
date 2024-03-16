package com.example.nutriblend.Modules.Recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.Modules.Recipes.Adapter.RecipesRecyclerAdapter
import com.example.nutriblend.databinding.FragmentRecipesBinding
import com.google.firebase.auth.FirebaseAuth


class RecipesFragment : Fragment() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var recipesRecyclerView: RecyclerView? = null
    private var adapter: RecipesRecyclerAdapter? = null
    private var progressBar: ProgressBar? = null
    private var _binding:  FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RecipesViewModel

    private val args: RecipesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[RecipesViewModel::class.java]

        progressBar = binding.progressBar
        progressBar?.visibility = View.VISIBLE

        if (args.isMyRecipes) {
            viewModel.recipes = Model.instance.getMyRecipes(auth.currentUser?.uid!!)
        } else {
            viewModel.recipes = Model.instance.getAllRecipes()
        }

        recipesRecyclerView = binding.rvRecipesFragmentList
        recipesRecyclerView?.setHasFixedSize(true)

        // set layout manager
        recipesRecyclerView?.layoutManager = LinearLayoutManager(context)

        // set the adapter
        recipesRecyclerView?.adapter = RecipesRecyclerAdapter(viewModel.recipes?.value)

        adapter = RecipesRecyclerAdapter(viewModel.recipes?.value)

        adapter?.listener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val recipe = viewModel.recipes?.value?.get(position)

                recipe?.let {
                    val action = RecipesFragmentDirections.actionRecipesFragmentToRecipeFragment(it.id).setRecipeIdArgRecipeFragment(it.id)
                    Navigation.findNavController(view).navigate(action)
                }
            }

            override fun onRecipeClicked(recipe: Recipe?) {
                Log.i("TAG","RecipesRecyclerAdapter: RECIPE $recipe")
            }
        }

        recipesRecyclerView?.adapter = adapter

        val addRecipeBtn: ImageButton = binding.iBtnRecipesFragmentAddRecipe // view.findViewById(R.id.iBtnRecipesFragmentAddRecipe)
        val action = Navigation.createNavigateOnClickListener(RecipesFragmentDirections.actionGlobalAddRecipeFragment())
        addRecipeBtn.setOnClickListener(action)

        binding.pullToRefresh.setOnRefreshListener {
            reloadRecipes()
        }

        Model.instance.recipesListLoadingState.observe(viewLifecycleOwner) { state ->
            binding.pullToRefresh.isRefreshing = state == Model.LoadingState.LOADING
        }

        viewModel.recipes?.observe(viewLifecycleOwner) { recipes ->
            adapter?.recipes = recipes
            adapter?.notifyDataSetChanged()
            progressBar?.visibility = View.GONE
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        reloadRecipes()
    }
    private fun reloadRecipes() {
        progressBar?.visibility = View.VISIBLE
        Model.instance.refreshAllRecipes()
        progressBar?.visibility = View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

interface OnItemClickListener{
    fun onItemClick(position: Int) // Recipe
    fun onRecipeClicked(recipe: Recipe?)
}