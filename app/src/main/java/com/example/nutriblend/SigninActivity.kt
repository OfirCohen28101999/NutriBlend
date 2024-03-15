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

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signinButton: Button
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        viewModel = ViewModelProvider(this)[SigninViewModel::class.java]

        bindElements()
        initListeners()
        initObservers()
    }

    private fun bindElements(){
        emailEditText = findViewById(R.id.signinEmailEditText)
        passwordEditText = findViewById(R.id.signinPasswordEditText)
        signinButton = findViewById(R.id.signinActivitySigninBtn)
        signupButton = findViewById(R.id.signinActivitySignupBtn)
    }

    private fun initListeners(){
        signinButton.setOnClickListener {
            viewModel.signin(emailEditText.text.toString(), passwordEditText.text.toString())
        }
        signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun initObservers(){
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
    }
}