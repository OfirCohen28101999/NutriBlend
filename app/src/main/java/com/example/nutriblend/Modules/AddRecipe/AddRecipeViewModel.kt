package com.example.nutriblend.Modules.AddRecipe

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.api.ApiClient
import com.example.nutriblend.api.NutritionalInfoList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class AddRecipeViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    private val _isNewRecipeAddedSuccessfully = MutableLiveData<Boolean>()
    val isNewRecipeAddedSuccessfully: LiveData<Boolean> get() = _isNewRecipeAddedSuccessfully


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    var recipe: Recipe? = null

    fun addNewRecipe(imageUri: Uri?, title: String, ingredients: String, preparationSteps: String) {
        _isLoading.value = true
        val recipeId = UUID.randomUUID().toString()
        var imageUrl: String? = null

        if (imageUri != null) {
            val imageRef = storageReference.child("recipe images/$recipeId")

            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        imageUrl = uri.toString()

                        val call = ApiClient.apiService.getNutritionalInfoList(ingredients)
                        call.enqueue(object : Callback<NutritionalInfoList> {
                            override fun onResponse(
                                call: Call<NutritionalInfoList>,
                                response: Response<NutritionalInfoList>
                            ) {
                                if (response.isSuccessful) {
                                    response.body()?.let { nutritionalInfoList ->
                                        val newRecipe: Recipe = ApiClient.createRecipeWithNutritionalInfo(nutritionalInfoList,
                                            recipeId,
                                            title,
                                            ingredients,
                                            preparationSteps,
                                            imageUrl,
                                            auth.currentUser?.uid!!)
                                        Model.instance.addRecipe(newRecipe) {
                                            // todo: check if working when theres a strong internet connection
                                            _isNewRecipeAddedSuccessfully.postValue(true)
                                            recipe = newRecipe
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<NutritionalInfoList>, t: Throwable) {
                                // todo: check if working when theres a strong internet connection
                                throw t
                            }
                        })
                    }
                }
                .addOnFailureListener {
                    // todo: check if working when theres a strong internet connection
                    _isNewRecipeAddedSuccessfully.postValue(false)
                }
        }

        // if want to upload recipe && imageSuccess OR doesn't want to upload recipe image at all
        if ((imageUri != null && imageUrl != null) || (imageUri == null)) {

            val call = ApiClient.apiService.getNutritionalInfoList(ingredients)
            call.enqueue(object : Callback<NutritionalInfoList> {
                override fun onResponse(
                    call: Call<NutritionalInfoList>,
                    response: Response<NutritionalInfoList>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { nutritionalInfoList ->
                            val newRecipe: Recipe = ApiClient.createRecipeWithNutritionalInfo(nutritionalInfoList,
                                                                                                recipeId,
                                                                                                title,
                                                                                                ingredients,
                                                                                                preparationSteps,
                                                                                                imageUrl,
                                                                                                auth.currentUser?.uid!!)
                            Model.instance.addRecipe(newRecipe) {
                                // todo: check if working when theres a strong internet connection
                                _isNewRecipeAddedSuccessfully.postValue(true)
                                recipe = newRecipe
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<NutritionalInfoList>, t: Throwable) {
                    // todo: check if working when theres a strong internet connection
                    _isNewRecipeAddedSuccessfully.postValue(false)
                }
            })
        }

        _isLoading.value = false
    }
}