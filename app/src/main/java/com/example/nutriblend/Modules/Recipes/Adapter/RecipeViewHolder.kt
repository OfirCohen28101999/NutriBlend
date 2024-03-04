package com.example.nutriblend.Modules.Recipes.Adapter

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.Modules.Recipes.RecipesRecyclerViewActivity
import com.example.nutriblend.R

class RecipeViewHolder(val itemView: View, val listener: RecipesRecyclerViewActivity.OnItemClickListener?, var recipes: MutableList<Recipe>?): RecyclerView.ViewHolder(itemView){
    var nameTextView: TextView? = null
    var idTextView: TextView? = null
    var recipeCheckbox: CheckBox? = null
    var recipe: Recipe? = null

    init {
        nameTextView = itemView.findViewById(R.id.tvStudentListRowName)
        idTextView = itemView.findViewById(R.id.tvStudentListRowID)
        recipeCheckbox = itemView.findViewById(R.id.cbStudentListRow)

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
        nameTextView?.text = recipe?.title
        idTextView?.text = recipe?.id
        recipeCheckbox?.apply {
            isChecked = recipe?.isChecked ?: false
        }
    }
}