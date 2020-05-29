package com.jc.foodapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.jc.foodapp.R

class FaqFragment : Fragment() {

    lateinit var q1:TextView
    lateinit var q2:TextView
    lateinit var q3:TextView
    lateinit var q4:TextView
    lateinit var q5:TextView
    lateinit var q6:TextView
    lateinit var q7:TextView

    lateinit var a1:TextView
    lateinit var a2:TextView
    lateinit var a3:TextView
    lateinit var a4:TextView
    lateinit var a5:TextView
    lateinit var a6:TextView
    lateinit var a7:TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_faq, container, false)

        q1=view.findViewById(R.id.faqq1)
        q2=view.findViewById(R.id.faqq2)
        q3=view.findViewById(R.id.faqq3)
        q4=view.findViewById(R.id.faqq4)
        q5=view.findViewById(R.id.faqq5)
        q6=view.findViewById(R.id.faqq6)
        q7=view.findViewById(R.id.faqq7)


        a1=view.findViewById(R.id.faqa1)
        a2=view.findViewById(R.id.faqa2)
        a3=view.findViewById(R.id.faqa3)
        a4=view.findViewById(R.id.faqa4)
        a5=view.findViewById(R.id.faqa5)
        a6=view.findViewById(R.id.faqa6)
        a7=view.findViewById(R.id.faqa7)


        q1.text=" Q. Can we pay through online? "
        a1.text=" No, only Cash on delivery is available right now."

        q2.text=" Q. Do you deliver to all the places in the city?"
        a2.text=" Yes, there are no restrictions on distance  between the restaurant and your address. "

        q3.text=" Q. Can I register twice with the same mobile  number? "
        a3.text=" No, you can register only once using a mobile number. "

        q4.text=" Q. Can I register twice with the same email  address? "
        a4.text=" No, you can register only once using an email address."

        q5.text=" Q. I forgot my password.What should I do? "
        a5.text=" Click on forgot password in Loginscreen and enter  your registered mobile number and email address. "

        q6.text=" Q. Can we order an item twice in our cart? "
        a6.text=" No you can add an item only once in the cart while  making an order. "

        q7.text=" Q. Can we order from all the restaurants in the city? "
        a7.text=" No, you can order only from restaurants which have  tied up with FoodLand. "

        return view
    }

}
