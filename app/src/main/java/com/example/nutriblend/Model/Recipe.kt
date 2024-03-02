package com.example.nutriblend.Model

data class Recipe(  val title: String,
                    val ingredients: String,
                    val preparationSteps: String,
                    val id: String,
                    val imageUrl: String,
                    var creatingUserId: String,
                    var lastUpdated: Long? = null,
                    var isChecked: Boolean? = false)
