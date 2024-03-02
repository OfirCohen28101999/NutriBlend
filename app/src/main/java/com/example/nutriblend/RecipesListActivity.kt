package com.example.nutriblend

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

class RecipesListActivity : AppCompatActivity() {
    private var recipesListView: ListView? = null
    private var recipes: MutableList<Recipe>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_list)

        recipes = Model.instance.recipes
        recipesListView = findViewById(R.id.lvRecipesList)
        recipesListView?.adapter = RecipesListAdapter(recipes)

        // https://hdq-colman-ac.zoom.us/rec/play/Qcu4lZoZWb3OykjU0NMQ1RuPelYAGv0_UTIy6fUEeeNvZITJzNbjIflAkcL80rB3fK6RJoQEe9QvSAA7.pZO8uPoIjRqi80H8
        // 30503647 1:15:00
    }

    class RecipesListAdapter(val recipes: MutableList<Recipe>?): BaseAdapter() {
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