package com.example.nutriblend.Modules.AddRecipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.R
import java.util.UUID

class AddRecipeFragment : Fragment() {
    private var recipeTitleTextBox: EditText? = null
    private var recipeIngredientsTextBox: EditText? = null
    private var recipePreparationStepsTextBox: EditText? = null
    private var saveRecipeBtn: Button? = null
    private var cancelRecipeBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)
        setupUI(view)
        return view
    }

    private fun setupUI(view: View) {
        recipeTitleTextBox = view.findViewById(R.id.recipeTitleTextBox)
        recipeIngredientsTextBox = view.findViewById(R.id.recipeIngredientsTextBox)
        recipePreparationStepsTextBox = view.findViewById(R.id.recipePreparationStepsTextBox)
        saveRecipeBtn = view.findViewById(R.id.saveRecipeBtn)
        cancelRecipeBtn = view.findViewById(R.id.cancelRecipeBtn)

        cancelRecipeBtn?.setOnClickListener{
            Navigation.findNavController(it).popBackStack(R.id.recipesFragment, false)
        }
        saveRecipeBtn?.setOnClickListener{
            val recipeTitle = recipeTitleTextBox?.text.toString()
            val recipeIngredients = recipeIngredientsTextBox?.text.toString()
            val recipePreparationSteps = recipePreparationStepsTextBox?.text.toString()

            val recipe = Recipe(
                UUID.randomUUID().toString(),
                recipeTitle,
                recipeIngredients,
                recipePreparationSteps,
                "image.url",
                "ofirCohenUserId")

            Model.instance.addRecipe(recipe){
                Toast.makeText(context, "recipe: $recipeTitle saved successfully", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(it).popBackStack(R.id.recipesFragment, false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }
}