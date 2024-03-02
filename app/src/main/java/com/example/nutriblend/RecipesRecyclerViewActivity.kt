package com.example.nutriblend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.Recipe

class RecipesRecyclerViewActivity : AppCompatActivity() {
    var recipesRecyclerView: RecyclerView? = null
    private var recipes: MutableList<Recipe>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_recycler_view)

        recipes = Model.instance.recipes
        recipesRecyclerView = findViewById(R.id.rvStudentRecyclerList)
        recipesRecyclerView?.setHasFixedSize(true)

        // set layout manager
        recipesRecyclerView?.layoutManager = LinearLayoutManager(this)

        // set the adapter
        recipesRecyclerView?.adapter = RecipesRecyclerAdapter()

        val adapter = RecipesRecyclerAdapter()
        adapter.listener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.i("TAG","RecipesRecyclerAdapter: POSITION CLICKED ${position}")
            }

            override fun onRecipeClicked(recipe: Recipe?) {
                Log.i("TAG","RecipesRecyclerAdapter: RECIPE ${recipe}")            }
        }

        recipesRecyclerView?.adapter = adapter
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int) // Recipe
        fun onRecipeClicked(recipe: Recipe?)
    }

    inner class RecipeViewHolder(val itemView: View, val listener: OnItemClickListener?): RecyclerView.ViewHolder(itemView){
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
    inner class RecipesRecyclerAdapter: RecyclerView.Adapter<RecipeViewHolder>() {

        var listener: OnItemClickListener? = null
        override fun getItemCount(): Int = recipes?.size ?: 0
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_layout_row,parent,false)
            return RecipeViewHolder(itemView, listener)
        }

        override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
            val recipe = recipes?.get(position)
            holder.bind(recipe)
        }

    }
}