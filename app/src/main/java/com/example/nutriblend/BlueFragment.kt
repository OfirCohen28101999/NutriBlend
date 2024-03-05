package com.example.nutriblend

import android.graphics.BlurMaskFilter.Blur
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation

class BlueFragment : Fragment() {

    private var textView: TextView? = null
    private var title: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_blue, container, false)

        // using navigation action param
        val blueTitle = arguments?.let {
            BlueFragmentArgs.fromBundle(it).titleArgBlueFragment
        }

        textView = view.findViewById(R.id.tvBlueFragmentTitle)
        textView?.text = blueTitle ?: "please assign a title"

        val backButton = view.findViewById<Button>(R.id.btnBlueFragmentBack)
        backButton.setOnClickListener{
            Navigation.findNavController(view).popBackStack()
        }

        return view
    }
}