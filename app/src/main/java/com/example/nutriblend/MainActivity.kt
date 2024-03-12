package com.example.nutriblend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()
    var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment: NavHostFragment? = supportFragmentManager.findFragmentById(R.id.navHostMain) as? NavHostFragment
        navController = navHostFragment?.navController
        navController?.let {
            NavigationUI.setupActionBarWithNavController(this,it)
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.mainActivityBottomNavigationView)
        navController?.let {
            NavigationUI.setupWithNavController(bottomNavigationView,it)
        }

        if (auth.currentUser == null) {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish()
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
