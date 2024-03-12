package com.example.nutriblend.Modules.Auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SigninViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _signinSuccess = MutableLiveData<Boolean>()
    val signinSuccess: LiveData<Boolean>
        get() = _signinSuccess

    fun signin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _signinSuccess.value = task.isSuccessful
            }
    }
}