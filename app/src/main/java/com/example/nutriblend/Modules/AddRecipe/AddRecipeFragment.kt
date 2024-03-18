package com.example.nutriblend.Modules.AddRecipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.squareup.picasso.Picasso
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.nutriblend.R
import com.example.nutriblend.databinding.FragmentAddRecipeBinding

class AddRecipeFragment : Fragment() {
    private var _binding:  FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AddRecipeViewModel

    private lateinit var recipeImageView: ImageView
    private lateinit var recipeTitleTextBox: EditText
    private lateinit var recipeIngredientsTextBox: EditText
    private lateinit var recipePreparationStepsTextBox: EditText
    private lateinit var saveRecipeBtn: Button
    private lateinit var cancelRecipeBtn: Button
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[AddRecipeViewModel::class.java]

        setupUI(view)

        return view
    }

    private fun setupUI(view: View) {
        bindElements()
        initListeners()
        observeActionStatuses(view)
    }

    private fun bindElements() {
        recipeImageView = binding.recipeImage
        recipeTitleTextBox = binding.recipeTitleTextBox
        recipeIngredientsTextBox = binding.recipeIngredientsTextBox
        recipePreparationStepsTextBox = binding.recipePreparationStepsTextBox
        saveRecipeBtn =  binding.saveRecipeBtn
        cancelRecipeBtn = binding.cancelRecipeBtn
        progressBar = binding.progressBarAddRecipeFragment
        progressBar.visibility = View.GONE
    }

    private fun initListeners() {
        recipeImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            getContent.launch(intent)
        }

        cancelRecipeBtn.setOnClickListener{
            Navigation.findNavController(it).popBackStack(R.id.recipesFragment, false)
        }

        saveRecipeBtn.setOnClickListener{
            val recipeTitle = recipeTitleTextBox.text.toString()
            val recipeIngredients = recipeIngredientsTextBox.text.toString()
            val recipePreparationSteps = recipePreparationStepsTextBox.text.toString()

            viewModel.addNewRecipe(selectedImageUri,recipeTitle, recipeIngredients, recipePreparationSteps)
        }
    }

    private fun observeActionStatuses(view: View) {
        viewModel.isNewRecipeAddedSuccessfully.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Recipe ${recipeTitleTextBox.text} uploaded successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Failed to add Recipe ${recipeTitleTextBox.text}", Toast.LENGTH_SHORT).show()
            }

            Navigation.findNavController(view).popBackStack(R.id.recipesFragment, false)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}