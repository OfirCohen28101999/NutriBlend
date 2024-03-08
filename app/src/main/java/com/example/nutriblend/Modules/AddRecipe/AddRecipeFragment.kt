package com.example.nutriblend.Modules.AddRecipe

import android.os.Bundle
import android.util.Log
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
import com.example.nutriblend.api.ApiClient
import com.example.nutriblend.api.NutritionalInfo
import com.example.nutriblend.api.NutritionalInfoList
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

            val call = ApiClient.apiService.getNutritionalInfoList(recipeIngredients)
            call.enqueue(object : Callback<NutritionalInfoList> {
                override fun onResponse(call: Call<NutritionalInfoList>, response: Response<NutritionalInfoList>) {
                    if (response.isSuccessful) {
                        val nutritionalInfoList = response.body()

                        Log.i("TAG", nutritionalInfoList.toString())

                        nutritionalInfoList?.let { nutritionalInfoList ->
                            val summedUpNutritionalInfo: NutritionalInfo = ApiClient.sumUp(nutritionalInfoList)

                            // Handle the retrieved post data
                            Log.i("TAG", summedUpNutritionalInfo.toString())

                            val recipe = Recipe(
                                id = UUID.randomUUID().toString(),
                                title = recipeTitle,
                                ingredients = recipeIngredients,
                                preparationSteps = recipePreparationSteps,
                                imageUrl = "image.url",
                                creatingUserId = "ofirCohenUserId",
                                calories = summedUpNutritionalInfo.calories,
                                sugar_g = summedUpNutritionalInfo.sugar_g,
                                fiber_g = summedUpNutritionalInfo.fiber_g,
                                sodium_mg = summedUpNutritionalInfo.sodium_mg,
                                potassium_mg = summedUpNutritionalInfo.potassium_mg,
                                fat_saturated_g = summedUpNutritionalInfo.fat_saturated_g,
                                fat_total_g = summedUpNutritionalInfo.fat_total_g,
                                cholesterol_mg = summedUpNutritionalInfo.cholesterol_mg,
                                protein_g = summedUpNutritionalInfo.protein_g,
                                carbohydrates_total_g = summedUpNutritionalInfo.carbohydrates_total_g
                            )

                            Log.i("TAG", recipe.toString())

                            Model.instance.addRecipe(recipe) {
                                Toast.makeText(context, "recipe: $recipeTitle saved successfully", Toast.LENGTH_SHORT).show()
                                Navigation.findNavController(it).popBackStack(R.id.recipesFragment, false)
                            }
                        }
                    } else {
                        // Handle error
                        Toast.makeText(context, "recipe: $recipeTitle save failed, could not retrieve nutritional data", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(it).popBackStack(R.id.recipesFragment, false)
                    }
                }

                override fun onFailure(call: Call<NutritionalInfoList>, t: Throwable) {
                    // Handle failure
                    Toast.makeText(context, "recipe: $recipeTitle save failed, could not retrieve nutritional data", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(it).popBackStack(R.id.recipesFragment, false)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }
}