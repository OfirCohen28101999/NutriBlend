package com.example.nutriblend.Modules.Recipes.Adapter

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.Modules.Recipes.OnItemClickListener
import com.example.nutriblend.R
import com.squareup.picasso.Picasso

class RecipeViewHolder(val itemView: View, val listener: OnItemClickListener?, var recipes: List<Recipe>?): RecyclerView.ViewHolder(itemView){
    private var recipeImageView: ImageView
    private var recipeTitleTextView: TextView

    var recipe: Recipe? = null

    init {
        recipeTitleTextView = itemView.findViewById(R.id.tvRecipeListRowTitle)
        recipeImageView = itemView.findViewById(R.id.ivRecipeListRowImage)

        itemView.setOnClickListener {
            Log.i("TAG","RecipeViewHolder: Position clicked $adapterPosition")

            listener?.onItemClick(adapterPosition)
            listener?.onRecipeClicked(recipe)
        }
    }

    fun bind(recipe: Recipe?) {
        this.recipe = recipe
        recipeTitleTextView.text = recipe?.title

        if(recipe?.imageUrl != null){
            Picasso.get().load(recipe.imageUrl).into(recipeImageView)
        }
    }
}