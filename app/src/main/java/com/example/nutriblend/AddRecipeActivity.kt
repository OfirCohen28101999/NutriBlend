package com.example.nutriblend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddRecipeActivity : AppCompatActivity() {

    private var recipeTitleTextBox: EditText? = null
    private var recipeIngredientsTextBox: EditText? = null
    private var recipePreparationStepsTextBox: EditText? = null
    private var saveRecipeBtn: Button? = null
    private var cancelRecipeBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        setupUI()
    }

    private fun setupUI() {
        recipeTitleTextBox = findViewById(R.id.recipeTitleTextBox)
        recipeIngredientsTextBox = findViewById(R.id.recipeIngredientsTextBox)
        recipePreparationStepsTextBox = findViewById(R.id.recipePreparationStepsTextBox)
        saveRecipeBtn = findViewById(R.id.saveRecipeBtn)
        cancelRecipeBtn = findViewById(R.id.cancelRecipeBtn)

        cancelRecipeBtn?.setOnClickListener{finish()}
        saveRecipeBtn?.setOnClickListener{onSaveRecipeClicked()}
    }

    private fun onSaveRecipeClicked() {
        val recipeTitle = recipeTitleTextBox?.text.toString()
        Toast.makeText(this, "recipe: $recipeTitle saved successfully", Toast.LENGTH_SHORT).show()
    }
}