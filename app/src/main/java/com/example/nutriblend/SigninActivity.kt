package com.example.nutriblend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.nutriblend.Modules.Auth.SigninViewModel

class SigninActivity : AppCompatActivity() {

    private lateinit var viewModel: SigninViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        viewModel = ViewModelProvider(this)[SigninViewModel::class.java]

        viewModel.signinSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val signinButton = findViewById<Button>(R.id.signinActivitySigninBtn)
        signinButton.setOnClickListener {
            signinUser()
        }

        val signupButton = findViewById<Button>(R.id.signinActivitySignupBtn)
        signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun signinUser() {
        val emailEditText = findViewById<EditText>(R.id.signinEmailEditText)
        val passwordEditText = findViewById<EditText>(R.id.signinPasswordEditText)

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        viewModel.signin(email, password)
    }
}