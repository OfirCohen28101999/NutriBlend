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
    private lateinit var signUpButton: Button
    private lateinit var signInButton: Button
    private lateinit var userNameEditText: EditText
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

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

        bindElements()
        initListeners()
        initObservers()
    }

    private fun bindElements() {
        userImageView = findViewById(R.id.userImageView)
        signUpButton = findViewById(R.id.signupActivitySignupBtn)
        signInButton = findViewById(R.id.signupActivitysigninBtn)
        userNameEditText = findViewById(R.id.usernameEditText)
        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.signUpPasswordEditText)
    }

    private fun initListeners() {
        signUpButton.setOnClickListener {
            signupUser()
        }

        signInButton.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }

        userImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            getContent.launch(intent)
        }
    }

    private fun initObservers(){
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
        val userName: String = userNameEditText.text.toString()
        val firstName: String = firstNameEditText.text.toString()
        val lastName: String = lastNameEditText.text.toString()
        val email: String = emailEditText.text.toString()
        val password: String = passwordEditText.text.toString()

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8) {
            viewModel.signupUser(userName, firstName, lastName, email, password, selectedImageUri)
        } else {
            Toast.makeText(baseContext, "Password must be more then 6 characters/Email not valid", Toast.LENGTH_SHORT).show()
        }
    }
}