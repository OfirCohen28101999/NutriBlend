package com.example.nutriblend.Modules.Recipes.Adapter

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.Modules.Recipes.OnItemClickListener
import com.example.nutriblend.R

class RecipeViewHolder(val itemView: View, val listener: OnItemClickListener?, var recipes: List<Recipe>?): RecyclerView.ViewHolder(itemView){
    var recipeTitleTextView: TextView? = null
    var recipeCheckbox: CheckBox? = null
    var recipe: Recipe? = null

    init {
        recipeTitleTextView = itemView.findViewById(R.id.tvRecipeListRowTitle)
        recipeCheckbox = itemView.findViewById(R.id.cbRecipeListRow)

        recipeCheckbox?.setOnClickListener{
            val recipe = recipes?.get(adapterPosition)
            recipe?.isChecked = recipeCheckbox?.isChecked ?: false
        }

        itemView.setOnClickListener {
            Log.i("TAG","RecipeViewHolder: Position clicked $adapterPosition")

            listener?.onItemClick(adapterPosition)
            listener?.onRecipeClicked(recipe)
        }
    }

    fun bind(recipe: Recipe?) {
        this.recipe = recipe
        recipeTitleTextView?.text = recipe?.title
        recipeCheckbox?.apply {
            isChecked = recipe?.isChecked ?: false
        }
    }
}