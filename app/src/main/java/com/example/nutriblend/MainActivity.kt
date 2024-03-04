package com.example.nutriblend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.nutriblend.Modules.Recipes.RecipesFragment

class MainActivity : AppCompatActivity() {

    var fragmentOne: RecipesFragment? = null
    var fragmentTwo: BlueFragment? = null
    var fragmentThree: BlueFragment? = null
    var fragmentFour: BlueFragment? = null

    var buttonOne: Button? = null
    var buttonTwo: Button? = null
    var buttonThree: Button? = null
    var buttonFour: Button? = null

    var currentlyDisplayedFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentOne = RecipesFragment()
        fragmentTwo = BlueFragment.newInstance("two")
        fragmentThree = BlueFragment.newInstance("three")
        fragmentFour = BlueFragment.newInstance("four")

        buttonOne = findViewById(R.id.btnMainTabOne)
        buttonTwo = findViewById(R.id.btnMainTabTwo)
        buttonThree = findViewById(R.id.btnMainTabThree)
        buttonFour = findViewById(R.id.btnMainTabFour)

        buttonOne?.setOnClickListener{
            fragmentOne?.let {
                displayBlueFragment(it)
            }
        }

        buttonTwo?.setOnClickListener{
            fragmentTwo?.let {
                displayBlueFragment(it)
            }
        }

        buttonThree?.setOnClickListener{
            fragmentThree?.let {
                displayBlueFragment(it)
            }
        }

        buttonFour?.setOnClickListener{
            fragmentFour?.let {
                displayBlueFragment(it)
            }
        }

        fragmentOne?.let {
            displayBlueFragment(it)
        }
    }
    fun displayBlueFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.flMainFragment, fragment)
        currentlyDisplayedFragment?.let {
            transaction.remove(it)
        }
        transaction.addToBackStack("tag")
        transaction.commit()
        currentlyDisplayedFragment = fragment
    }
}
