package com.example.nutriblend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.nutriblend.Modules.Recipes.RecipesFragment

class MainActivity : AppCompatActivity() {
    var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment: NavHostFragment? = supportFragmentManager.findFragmentById(R.id.navHostMain) as? NavHostFragment
        navController = navHostFragment?.navController
        navController?.let {
            NavigationUI.setupActionBarWithNavController(this,it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
//                navController?.popBackStack()
                navController?.navigateUp()
                true
            }
//            R.id.addRecipeFragment -> {
//                navController?.navigate(R.id.action_global_addRecipeFragment)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
            // can be used in case the id of the menu item & nav graph dest are equal
            else -> navController?.let { NavigationUI.onNavDestinationSelected(item,it) } ?: super.onOptionsItemSelected(item)
        }
    }
}
