package com.example.nutriblend.Modules.Auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class SignupViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference
    private val _signupSuccess = MutableLiveData<Boolean>()
    val signupSuccess: LiveData<Boolean> get() = _signupSuccess

    fun signupUser(userName: String, firstName: String, lastName: String, email: String, password: String, imageUri: Uri?) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    createExtendedUser(userName, firstName,lastName, email, imageUri)
                } else {
                    _signupSuccess.value = false
                }
            }
            .addOnFailureListener {
                _signupSuccess.value = false
            }
    }

    private fun createExtendedUser(userName: String, firstName: String, lastName: String, email: String, imageUri: Uri?) {
        val currentUserId: String = FirebaseAuth.getInstance().currentUser!!.uid

        val imageName = UUID.randomUUID().toString()
        val imageRef = storageReference.child("post images/$imageName.jpg")

        if (imageUri != null) {
            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        Model.instance.signupNewUser(User(id = currentUserId, username = userName.lowercase(), firstName = firstName, lastName = lastName, email = email, avatarUrl = uri.toString(), createdAt = System.currentTimeMillis())) {
                            _signupSuccess.value = true
                        }
                    }
                }
                .addOnFailureListener {
                    _signupSuccess.value = false
                }
        } else {
            Model.instance.signupNewUser(User(id = currentUserId, username = userName.lowercase(), firstName = firstName, lastName = lastName, email = email, avatarUrl = null, createdAt = System.currentTimeMillis())) {
                _signupSuccess.value = true
            }
        }
    }
}