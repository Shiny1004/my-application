package com.jc.foodapp.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jc.foodapp.model.HomeRestaurant
import com.jc.foodapp.R
import com.jc.foodapp.adapter.HomeAdapter
import com.jc.foodapp.adapter.HomeAdapter2
import technolifestyle.com.imageslider.FlipperLayout
import technolifestyle.com.imageslider.FlipperView


class HomeFragment : Fragment() {

    lateinit var  l_flipper :FlipperLayout
    lateinit var layoutManager1: GridLayoutManager
    lateinit var layoutManager2: GridLayoutManager
    lateinit var recyclerhome1:RecyclerView
    lateinit var recyclerhome2:RecyclerView
    lateinit var recyclerAdapter1:HomeAdapter
    lateinit var recyclerAdapter2: HomeAdapter2


    val images= arrayOf(R.drawable.foodimage,R.drawable.foodimage1,R.drawable.foodimage2,
        R.drawable.foodimage3,R.drawable.foodimage4,R.drawable.foodimage5,R.drawable.foodimage6)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_home, container, false)
        val userid=this.arguments!!.getString("user_id","00")


        val homeresarray= arrayListOf<HomeRestaurant>(
            HomeRestaurant(
                userid,
                "10",
                "Central Terk",
                "5",
                "https://images.pexels.com/photos/434213/pexels-photo-434213.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
            ),
            HomeRestaurant(
                userid,
                "2",
                "Garbar Burgers",
                "4.6",
                "https://images.pexels.com/photos/1639565/pexels-photo-1639565.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
            ),
            HomeRestaurant(
                userid,
                "13",
                "Pizza Put",
                "4.4",
                "https://images.pexels.com/photos/724216/pexels-photo-724216.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"
            ),
            HomeRestaurant(
                userid,
                "4",
                "Heera Mahal",
                "4.2",
                "https://images.pexels.com/photos/1300972/pexels-photo-1300972.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
            ),
            HomeRestaurant(
                userid,
                "17",
                "Askin' Poppins",
                "4.1",
                "https://images.pexels.com/photos/3631/summer-dessert-sweet-ice-cream.jpg?auto=compress&cs=tinysrgb&dpr=1&w=500"
            ),
            HomeRestaurant(
                userid,
                "1",
                "Pind Tadka",
                "4.1",
                "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
            )
        )

        val arraylist1= arrayListOf<HomeRestaurant>(
            HomeRestaurant(
                userid,
                "17",
                "Askin' Poppins",
                "4.1",
                "https://images.pexels.com/photos/3631/summer-dessert-sweet-ice-cream.jpg?auto=compress&cs=tinysrgb&dpr=1&w=500"),
            HomeRestaurant(
                userid,
                "5",
                "Smokin' Chik",
                " 4.0",
                "https://images.pexels.com/photos/265393/pexels-photo-265393.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"),
            HomeRestaurant(
                userid,
                "11",
                "Mitti ke Sandwiches",
                "4.0",
                "https://images.pexels.com/photos/1600711/pexels-photo-1600711.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940")
            )

        val arraylist2= arrayListOf<String>("Ice-creams from Askin' Poppins will melt in your mouth!","Have you tried this deceivingly delicious chicken?",
            "Grilled Sand from Mitti ke Sandwiches is the best-seller in the city!")


        recyclerhome1=view.findViewById(R.id.homerecyclerview1)
        recyclerhome2=view.findViewById(R.id.homerecyclerview2)

        l_flipper=view.findViewById(R.id.homeviewflipper)

        for(im in images)
        {
            val flipperimageview=ImageView(activity as Context)
           val flipperView=FlipperView(activity as Context)
            val drawable=ContextCompat.getDrawable(activity as Context,im)
            flipperView.setImageDrawable(drawable){
                flipperImageView, imageDrawable ->  flipperImageView.setImageDrawable(drawable)
            }
            flipperView.setDescriptionBackgroundColor(Color.TRANSPARENT)
            flipperView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            l_flipper.addFlipperView(flipperView)
            l_flipper.showInnerPagerIndicator()
            l_flipper.startAutoCycle(3)
            l_flipper.setIndicatorBackgroundColor(Color.TRANSPARENT)

        }
        layoutManager1= GridLayoutManager(context,1,GridLayoutManager.HORIZONTAL,false)
        layoutManager2= GridLayoutManager(context,1,GridLayoutManager.HORIZONTAL,false)

        recyclerAdapter1= HomeAdapter(activity as Context,homeresarray)

        recyclerAdapter2= HomeAdapter2(activity as Context,arraylist1,arraylist2)

        recyclerhome1.adapter=recyclerAdapter1
        recyclerhome1.layoutManager=layoutManager1

        recyclerhome2.adapter=recyclerAdapter2
        recyclerhome2.layoutManager=layoutManager2

        return view
    }

}


