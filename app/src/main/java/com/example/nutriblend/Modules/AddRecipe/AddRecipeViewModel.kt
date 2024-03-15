package com.example.nutriblend.Modules.AddRecipe

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
                    _isNewRecipeAddedSuccessfully.postValue(false)
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
                            val summedUpNutritionalInfo: NutritionalInfo = ApiClient.sumUp(nutritionalInfoList)
                            val newRecipe = Recipe(
                                id = recipeId,
                                title = title,
                                ingredients = ingredients,
                                preparationSteps = preparationSteps,
                                imageUrl = imageUrl,
                                creatingUserId = auth.currentUser?.uid!!,
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
                            Model.instance.addRecipe(newRecipe) {
                                _isNewRecipeAddedSuccessfully.postValue(true)
                                recipe = newRecipe
                            }
                        }
                    } else {
                        _isNewRecipeAddedSuccessfully.postValue(false)
                    }
                }

                override fun onFailure(call: Call<NutritionalInfoList>, t: Throwable) {
                    _isNewRecipeAddedSuccessfully.postValue(false)
                }
            })
        }

        _isLoading.value = false
    }
}