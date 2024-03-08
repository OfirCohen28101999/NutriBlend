package com.example.nutriblend.Modules.Recipes.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.Modules.Recipes.OnItemClickListener
import com.example.nutriblend.R

class RecipesRecyclerAdapter(var recipes: List<Recipe>?): RecyclerView.Adapter<RecipeViewHolder>() {

    var listener: OnItemClickListener? = null
    override fun getItemCount(): Int = recipes?.size ?: 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_layout_row,parent,false)
        return RecipeViewHolder(itemView, listener, recipes)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes?.get(position)
        holder.bind(recipe)
    }

}