package com.example.nutriblend.Modules.Recipe

import android.net.Uri
import android.util.Log
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


class RecipeViewModel : ViewModel() {
    var recipe: LiveData<Recipe?>?= null
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    private val _isRecipeUpdatedSuccessefully = MutableLiveData<Boolean>()
    val isRecipeUpdatedSuccessefully: LiveData<Boolean> get() = _isRecipeUpdatedSuccessefully

    private val _isRecipeDeletedSuccessefully = MutableLiveData<Boolean>()
    val isRecipeDeletedSuccessefully: LiveData<Boolean> get() = _isRecipeDeletedSuccessefully

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun updateRecipe(recipeId: String,previousImgUrl: String?,imageUri: Uri?, title: String, ingredients: String, preparationSteps: String) {
        _isLoading.value = true
        var imageUrl: String? = null

        // todo: delete previousImgUrl if not null

        if (imageUri != null) {
            val imageRef = storageReference.child("images/$recipeId.jpg")

            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        imageUrl = uri.toString()
                    }
                }
                .addOnFailureListener { e ->
                    _isRecipeUpdatedSuccessefully.postValue(false)
                }
        }

        // if want to upload recipe && imageSuccess OR doesn't want to upload recipe image at all
        if ((imageUri != null && imageUrl != null) || (imageUri == null)) {

            // get nutritional values from calorieNinja api
            val call = ApiClient.apiService.getNutritionalInfoList(ingredients)
            call.enqueue(object : Callback<NutritionalInfoList> {
                override fun onResponse(
                    call: Call<NutritionalInfoList>,
                    response: Response<NutritionalInfoList>
                ) {
                    if (response.isSuccessful) {
                        val nutritionalInfoList = response.body()

                        Log.i("TAG", nutritionalInfoList.toString())

                        nutritionalInfoList?.let { nutritionalInfoList ->
                            val summedUpNutritionalInfo: NutritionalInfo =
                                ApiClient.sumUp(nutritionalInfoList)

                            Log.i("TAG", summedUpNutritionalInfo.toString())

                            val updatedRecipe = Recipe(
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

                            Log.i("TAG", updatedRecipe.toString())

                            Model.instance.updateRecipe(updatedRecipe) {
                                _isRecipeUpdatedSuccessefully.postValue(true)
                            }
                        }
                    } else {
                        _isRecipeUpdatedSuccessefully.postValue(false)
                    }
                }

                override fun onFailure(call: Call<NutritionalInfoList>, t: Throwable) {
                    _isRecipeUpdatedSuccessefully.postValue(false)
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
//                    _isRecipeUpdatedSuccessefully.postValue(false)
//                }
//        }

        Model.instance.deleteRecipe(recipe, { _isRecipeDeletedSuccessefully.postValue(true)})

        _isLoading.value = false
    }


}