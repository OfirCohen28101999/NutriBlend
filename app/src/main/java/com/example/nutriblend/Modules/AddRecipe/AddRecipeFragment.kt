package com.example.nutriblend.Modules.AddRecipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.nutriblend.R

class AddRecipeFragment : Fragment() {
    private var recipeTitleTextBox: EditText? = null
    private var recipeIngredientsTextBox: EditText? = null
    private var recipePreparationStepsTextBox: EditText? = null
    private var saveRecipeBtn: Button? = null
    private var cancelRecipeBtn: Button? = null
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
            Navigation.findNavController(it).popBackStack()
        }
        saveRecipeBtn?.setOnClickListener{onSaveRecipeClicked()}
    }

    private fun onSaveRecipeClicked() {
        val recipeTitle = recipeTitleTextBox?.text.toString()
        Toast.makeText(context, "recipe: $recipeTitle saved successfully", Toast.LENGTH_SHORT).show()
    }
}