package com.jc.foodapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jc.foodapp.R

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {

    lateinit var txt:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_about, container, false)

        txt=view.findViewById(R.id.abouttxt)

        txt.text="FoodLand is a start-up led by a group of friends. Our main aim is to " +
                "deliver food from a wide variety of restaurants to a user in his city. "+
                "Currently, we don't support online transactions. Cash on delivery is the "+
                "only option available. We ensure to safely deliver your order."+
                "You can give feedback by writing to www.foodland.com. "+"Healthy Food! Happy People!"
        return view
    }

}
