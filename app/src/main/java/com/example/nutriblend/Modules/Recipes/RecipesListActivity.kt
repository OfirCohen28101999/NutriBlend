package com.example.nutriblend.Modules.Recipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.Recipe
import com.example.nutriblend.R

class RecipesListActivity : AppCompatActivity() {
    private var recipesListView: ListView? = null
    private var recipes: List<Recipe>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_list)

        Model.instance.getAllRecipes { recipes ->
            this.recipes = recipes
        }

        recipesListView = findViewById(R.id.lvRecipesList)
        recipesListView?.adapter = RecipesListAdapter(recipes)
    }

    class RecipesListAdapter(val recipes: List<Recipe>?): BaseAdapter() {
        override fun getCount(): Int = recipes?.size ?: 0
        override fun getItem(position: Int): Any {
            TODO("Not yet implemented")
        }

        override fun getItemId(position: Int): Long {
            TODO("Not yet implemented")
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val recipe = recipes?.get(position)
            var view: View? = null

            if(convertView == null){
                view = LayoutInflater.from(parent?.context).inflate(R.layout.recipe_layout_row,parent,false)
                val recipeCheckbox: CheckBox? = view?.findViewById(R.id.cbStudentListRow)
                recipeCheckbox?.setOnClickListener{
                    (recipeCheckbox?.tag as? Int)?.let {tag ->
                        val recipe = recipes?.get(tag)
                        recipe?.isChecked = recipeCheckbox?.isChecked ?: false
                    }
                }
            }

            view = view ?: convertView

            val nameTextView: TextView? = view?.findViewById(R.id.tvStudentListRowName)
            val idTextView: TextView? = view?.findViewById(R.id.tvStudentListRowID)
            val recipeCheckbox: CheckBox? = view?.findViewById(R.id.cbStudentListRow)

            nameTextView?.text = recipe?.title
            idTextView?.text = recipe?.id
            recipeCheckbox?.apply {
                isChecked = recipe?.isChecked ?: false
                tag = position
            }
            return view!!
        }

    }
}