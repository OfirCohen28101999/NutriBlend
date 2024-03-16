package com.example.nutriblend.Modules.Recipe

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


class RecipeViewModel : ViewModel() {
    var recipe: LiveData<Recipe?>?= null
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    private val _isRecipeUpdatedSuccessfully = MutableLiveData<Boolean>()
    val isRecipeUpdatedSuccessfully: LiveData<Boolean> get() = _isRecipeUpdatedSuccessfully

    private val _isRecipeDeletedSuccessfully = MutableLiveData<Boolean>()
    val isRecipeDeletedSuccessfully: LiveData<Boolean> get() = _isRecipeDeletedSuccessfully

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun updateRecipe(recipe: Recipe,imageUri: Uri?, title: String, ingredients: String, preparationSteps: String) {
        _isLoading.value = true
        var imageUrl: String? = null

        if (imageUri != null) {
            val imageRef = storageReference.child("recipe images/${recipe.id}")

            imageRef.delete().addOnCompleteListener {
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
                                            val updatedRecipe: Recipe =
                                                ApiClient.createRecipeWithNutritionalInfo(nutritionalInfoList,
                                                    recipe.id,
                                                    title,
                                                    ingredients,
                                                    preparationSteps,
                                                    imageUrl,
                                                    auth.currentUser?.uid!!)
                                            Model.instance.updateRecipe(updatedRecipe) {
                                                _isRecipeUpdatedSuccessfully.postValue(true)
                                            }
                                        }
                                    } else {
                                        _isRecipeUpdatedSuccessfully.postValue(false)
                                    }
                                }

                                override fun onFailure(call: Call<NutritionalInfoList>, t: Throwable) {
//                                    _isRecipeUpdatedSuccessfully.postValue(false)
                                }
                            })
                        }
                    }
                    .addOnFailureListener {
                        _isRecipeUpdatedSuccessfully.postValue(false)
                    }
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
                            val updatedRecipe: Recipe =
                                ApiClient.createRecipeWithNutritionalInfo(nutritionalInfoList,
                                                                            recipe.id,
                                                                            title,
                                                                            ingredients,
                                                                            preparationSteps,
                                                                            imageUrl,
                                                                            auth.currentUser?.uid!!)
                            Model.instance.updateRecipe(updatedRecipe) {
                                _isRecipeUpdatedSuccessfully.postValue(true)
                            }
                        }
                    }
//                    else {
//                        _isRecipeUpdatedSuccessfully.postValue(false)
//                    }
                }

                override fun onFailure(call: Call<NutritionalInfoList>, t: Throwable) {
                    _isRecipeUpdatedSuccessfully.postValue(false)
                }
            })
        }

        _isLoading.value = false
    }

    fun deleteRecipe(recipe: Recipe) {
        _isLoading.value = true

        if(recipe.imageUrl != null) {
            val imageRef = storageReference.child("recipe images/${recipe.id}")
            imageRef.delete().addOnSuccessListener {
                Model.instance.deleteRecipe(recipe) { _isRecipeDeletedSuccessfully.postValue(true) }
            }.addOnFailureListener {
                _isRecipeUpdatedSuccessfully.postValue(false)
            }
        } else {
            Model.instance.deleteRecipe(recipe) { _isRecipeDeletedSuccessfully.postValue(true) }

        }

        _isLoading.value = false
    }
}