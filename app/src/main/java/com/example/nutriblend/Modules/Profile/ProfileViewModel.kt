package com.example.nutriblend.Modules.Profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileViewModel : ViewModel() {
    var user: LiveData<User?>?= null
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    private val _isUserUpdatedSuccessfully = MutableLiveData<Boolean>()
    val isUserUpdatedSuccessfully: LiveData<Boolean> get() = _isUserUpdatedSuccessfully

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun updateUserProfile(user: User, imageUri: Uri?, firstName: String, lastName: String, username: String) {
        _isLoading.value = true
        var imageUrl: String? = null

        if (imageUri != null) {
            val imageRef = storageReference.child("user images/${user.id}")

            imageRef.delete().addOnCompleteListener {
                imageRef.putFile(imageUri)
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            imageUrl = uri.toString()

                            val updatedUser = User(
                                id = user.id,
                                avatarUrl = imageUrl ?: user.avatarUrl,
                                email = user.email,
                                firstName = firstName,
                                lastName = lastName,
                                username = username
                            )

                            Model.instance.updateUserProfile(updatedUser) {
                                _isUserUpdatedSuccessfully.postValue(true)
                            }
                        }
                    }
                    .addOnFailureListener {
                        _isUserUpdatedSuccessfully.postValue(false)
                    }
            }
        }

        // if want to upload user img && imageSuccess OR doesn't want to update userimg at all
        if ((imageUri != null && imageUrl != null) || (imageUri == null)) {
            val updatedUser = User(
                id = user.id,
                avatarUrl = imageUrl ?: user.avatarUrl,
                email = user.email,
                firstName = firstName,
                lastName = lastName,
                username = username
            )

            Model.instance.updateUserProfile(updatedUser) {
                _isUserUpdatedSuccessfully.postValue(true)
            }
        }

        _isLoading.value = false
    }
}