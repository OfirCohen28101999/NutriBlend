package com.example.nutriblend

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.nutriblend.Modules.Auth.SignupViewModel

class SignupActivity : AppCompatActivity() {

    private lateinit var viewModel: SignupViewModel
    private lateinit var userImageView: ImageView
    private var selectedImageUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            selectedImageUri = data?.data
            userImageView.setImageURI(data?.data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]

        val signUpButton: Button = findViewById(R.id.signupActivitySignupBtn)
        signUpButton.setOnClickListener {
            signupUser()
        }

        val signinButton = findViewById<Button>(R.id.signupActivitysigninBtn)
        signinButton.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }

        setImage()

        viewModel.signupSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(baseContext, "Signup failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signupUser() {
        val userNameEditText: EditText = findViewById(R.id.usernameEditText)
        val firstNameEditText: EditText = findViewById(R.id.firstNameEditText)
        val lastNameEditText: EditText = findViewById(R.id.lastNameEditText)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.signUpPasswordEditText)

        val userName: String = userNameEditText.text.toString()
        val firstName: String = firstNameEditText.text.toString()
        val lastName: String = lastNameEditText.text.toString()
        val email: String = emailEditText.text.toString()
        val password: String = passwordEditText.text.toString()

        if (isEmailValid(email) && isPasswordValid(password)) {
            viewModel.signupUser(userName, firstName, lastName, email, password, selectedImageUri)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val isEmailPattern = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        if (!isEmailPattern) {
            Toast.makeText(baseContext, "Email not valid", Toast.LENGTH_SHORT).show()
        }

        return isEmailPattern
    }

    private fun isPasswordValid(password: String): Boolean {
        val isPasswordPattern = password.length >= 6

        if (!isPasswordPattern) {
            Toast.makeText(baseContext, "Password must be more then 6 characters", Toast.LENGTH_SHORT).show()
        }

        return isPasswordPattern
    }

    private fun setImage() {
        userImageView = findViewById(R.id.userImageView)
        userImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            getContent.launch(intent)
        }
    }
}