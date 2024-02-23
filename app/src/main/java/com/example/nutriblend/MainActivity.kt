package com.example.nutriblend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addRecipeButton: Button = findViewById(R.id.btnMainAddRecipe)
        addRecipeButton.setOnClickListener(::onAddRecipeButtonClicked)
    }

    fun onAddRecipeButtonClicked(view: View){
        // INTENT TO GO TO ADD RECIPE ACTIVITY
    }
}
