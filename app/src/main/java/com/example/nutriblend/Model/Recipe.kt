package com.example.nutriblend.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Recipe(  @PrimaryKey val id: String,
                    val title: String,
                    val ingredients: String,
                    val preparationSteps: String,
                    val imageUrl: String,
                    var creatingUserId: String,
                    var lastUpdated: Long? = null,
                    var isChecked: Boolean? = false)
