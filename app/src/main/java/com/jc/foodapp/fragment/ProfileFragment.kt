package com.jc.foodapp.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.jc.foodapp.R

class ProfileFragment : Fragment() {

    lateinit var profilename:TextView
    lateinit var profilemobileno:TextView
    lateinit var profileemail:TextView
    lateinit var profileaddress:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view=inflater.inflate(R.layout.fragment_profile, container, false)
        //this view creation is compulsory

        val b1=this.arguments?.getString("name")
        val b2=this.arguments?.getString("mobile_number")
        val b3=this.arguments?.getString("email")
        val b4=this.arguments?.getString("address")

        profilename=view.findViewById(R.id.profilename)
        profilemobileno= view.findViewById(R.id.profilemobileno)
        profileemail= view.findViewById(R.id.profileemail)
        profileaddress  = view.findViewById(R.id.profileaddress)

        profilename.text=b1
        profilemobileno.text="+91-${b2}"
        profileemail.text=b3
        profileaddress.text=b4

        return view
    }

}
