package com.example.nutriblend.Modules.Recipe

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.api.ApiClient
import com.example.nutriblend.api.NutritionalInfo
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

    fun updateRecipe(recipeId: String,previousImgUrl: String?,imageUri: Uri?, title: String, ingredients: String, preparationSteps: String) {
        _isLoading.value = true
        var imageUrl: String? = null

        // todo: delete previousImgUrl if not null & check storage logic

        if (imageUri != null) {
            val imageRef = storageReference.child("images/$recipeId.jpg")

            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        imageUrl = uri.toString()
                    }
                }
                .addOnFailureListener { e ->
                    _isRecipeUpdatedSuccessfully.postValue(false)
                }
        }

        // if want to upload recipe && imageSuccess OR doesn't want to upload recipe image at all
        if ((imageUri != null && imageUrl != null) || (imageUri == null)) {

            // get nutritional values from calorieNinja api todo: get rid of duplication somehow
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
                                                                            recipeId,
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
                    _isRecipeUpdatedSuccessfully.postValue(false)
                }
            })
        }

        _isLoading.value = false
    }

    fun deleteRecipe(recipe: Recipe) {
        _isLoading.value = true
        var imageUrl: String? = null

        // todo: delete previousImgUrl = recipe.imageUrl if not null

//        if (imageUri != null) {
//            val imageRef = storageReference.child("images/$recipeId.jpg")
//
//            imageRef.putFile(imageUri)
//                .addOnSuccessListener {
//                    imageRef.downloadUrl.addOnSuccessListener { uri ->
//                        imageUrl = uri.toString()
//                    }
//                }
//                .addOnFailureListener { e ->
//                    _isRecipeUpdatedSuccessfully.postValue(false)
//                }
//        }

        Model.instance.deleteRecipe(recipe) { _isRecipeDeletedSuccessfully.postValue(true) }

        _isLoading.value = false
    }


}