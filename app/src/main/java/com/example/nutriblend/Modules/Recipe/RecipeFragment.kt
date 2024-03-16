package com.example.nutriblend.Modules.Recipe

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.R
import com.example.nutriblend.databinding.FragmentRecipeBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class RecipeFragment : Fragment() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _binding:  FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RecipeViewModel

    private lateinit var recipeImageView: ImageView
    private lateinit var recipeTitleTextView: EditText
    private lateinit var recipeIngredientsTextView: EditText
    private lateinit var recipePrepStepsTextView: EditText
    private lateinit var caloriesValueTextView: TextView
    private lateinit var totalFatValueTextView: TextView
    private lateinit var satFatValueTextView: TextView
    private lateinit var proteinValueTextView: TextView
    private lateinit var sodiumValueTextView: TextView
    private lateinit var potassiumValueTextView: TextView
    private lateinit var carboValueTextView: TextView
    private lateinit var cholesterolValueTextView: TextView
    private lateinit var sugarValueTextView: TextView
    private lateinit var fiberValueTextView: TextView

    private lateinit var saveBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var progressBar: ProgressBar

    private var selectedImageUri: Uri? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                selectedImageUri = data?.data
                Picasso.get().load(selectedImageUri).into(recipeImageView)
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        val recipeId = arguments?.let {
            RecipeFragmentArgs.fromBundle(it).recipeIdArgRecipeFragment
        }

        viewModel.recipe = Model.instance.getRecipeById(recipeId!!)

        viewModel.recipe?.observe(viewLifecycleOwner) {
            setupUI(it, view)
        }

        return view
    }

    private fun setupUI(recipe: Recipe?, view: View) {
        bindElements()
        insertRecipeValues(recipe!!)
        changeEditViewBasedOnCurrentUser(recipe)
        initListeners(recipe)
        observeActionStatuses(view)
    }

    private fun bindElements(){
        recipeImageView = binding.recipeImageFragmentRecipe
        progressBar = binding.progressBarRecipeFragment
        saveBtn = binding.saveRecipeBtnFragmentRecipe
        deleteBtn = binding.deleteRecipeBtnFragmentRecipe
        recipeTitleTextView = binding.recipeTitleTextBoxFragmentRecipe
        recipeIngredientsTextView = binding.recipeIngredientsTextBoxFragmentRecipe
        recipePrepStepsTextView = binding.recipePreparationStepsTextBoxFragmentRecipe
        caloriesValueTextView = binding.caloriesValueTextView
        totalFatValueTextView = binding.totalFatValueTextView
        satFatValueTextView = binding.satFatValueTextView
        proteinValueTextView = binding.proteinValueTextView
        sodiumValueTextView = binding.sodiumValueTextView
        potassiumValueTextView = binding.potassiumValueTextView
        carboValueTextView = binding.carboValueTextView
        cholesterolValueTextView = binding.cholesterolValueTextView
        sugarValueTextView = binding.sugarValueTextView
        fiberValueTextView = binding.fiberValueTextView
        progressBar.visibility = View.GONE
    }

    private fun insertRecipeValues(recipe: Recipe){
        if(recipe.imageUrl != null){
            Picasso.get().load(recipe.imageUrl).into(recipeImageView)

        }

        recipeTitleTextView.setText(recipe.title)
        recipeIngredientsTextView.setText(recipe.ingredients)
        recipePrepStepsTextView.setText(recipe.preparationSteps)
        caloriesValueTextView.text = recipe.calories?.toString() ?: "0.000"
        totalFatValueTextView.text = recipe.fat_total_g?.toString() ?: "0.000"
        satFatValueTextView.text = recipe.fat_saturated_g?.toString() ?: "0.000"
        proteinValueTextView.text = recipe.protein_g?.toString() ?: "0.000"
        sodiumValueTextView.text = recipe.sodium_mg?.toString() ?: "0.000"
        potassiumValueTextView.text = recipe.potassium_mg?.toString() ?: "0.000"
        carboValueTextView.text = recipe.carbohydrates_total_g?.toString() ?: "0.000"
        cholesterolValueTextView.text = recipe.cholesterol_mg?.toString() ?: "0.000"
        sugarValueTextView.text = recipe.sugar_g?.toString() ?: "0.000"
        fiberValueTextView.text = recipe.fiber_g?.toString() ?: "0.000"
    }
    private fun changeEditViewBasedOnCurrentUser(recipe: Recipe) {
        if(auth.currentUser?.uid!! == recipe.creatingUserId) {
            saveBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE

            recipeImageView.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                getContent.launch(intent)
            }
        } else {
            recipeImageView.setOnClickListener(null)

            saveBtn.visibility = View.GONE
            deleteBtn.visibility = View.GONE

            recipeTitleTextView.isEnabled = false
            recipeTitleTextView.isFocusable = false
            recipeTitleTextView.isFocusableInTouchMode = false
            recipeTitleTextView.setTextColor(Color.BLACK)

            recipeIngredientsTextView.isEnabled = false
            recipeIngredientsTextView.isFocusable = false
            recipeIngredientsTextView.isFocusableInTouchMode = false
            recipeIngredientsTextView.setTextColor(Color.BLACK)

            recipePrepStepsTextView.isEnabled = false
            recipePrepStepsTextView.isFocusable = false
            recipePrepStepsTextView.isFocusableInTouchMode = false
            recipePrepStepsTextView.setTextColor(Color.BLACK)
        }
    }
    private fun initListeners(recipe: Recipe){
        deleteBtn.setOnClickListener{
            viewModel.deleteRecipe(recipe)
        }

        saveBtn.setOnClickListener {
            val recipeTitle = recipeTitleTextView.text.toString()
            val recipeIngredients = recipeIngredientsTextView.text.toString()
            val recipePreparationSteps = recipePrepStepsTextView.text.toString()

            viewModel.updateRecipe(recipe, selectedImageUri,recipeTitle, recipeIngredients, recipePreparationSteps)
        }
    }
    private fun observeActionStatuses(view: View){

        // TODO: bug: happens 3 times for some reason
        viewModel.isRecipeUpdatedSuccessfully.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Recipe ${recipeTitleTextView.text} updated successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Failed to update Recipe ${recipeTitleTextView.text}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isRecipeDeletedSuccessfully.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Recipe ${recipeTitleTextView.text} deleted successfully", Toast.LENGTH_SHORT)
                    .show()
                Navigation.findNavController(view).popBackStack(R.id.recipesFragment, false)
            } else {
                Toast.makeText(requireContext(), "Failed to delete Recipe ${recipeTitleTextView.text}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}