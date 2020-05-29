package com.jc.foodapp.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.jc.foodapp.R
import com.jc.foodapp.adapter.FavoritesAdapter
import com.jc.foodapp.database.RestaurantDatabase
import com.jc.foodapp.database.RestaurantEntity
import kotlinx.android.synthetic.*

class FavoritesFragment : Fragment() {

    lateinit var favrecyclerview:RecyclerView
    lateinit var favAdapter:FavoritesAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    lateinit var progressText:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_favorites, container, false)

        val userid=this.arguments!!.getString("user_id","00")

        favrecyclerview=view.findViewById(R.id.favrecyclerview)
        progressLayout=view.findViewById(R.id.favprogresslayout)
        progressBar=view.findViewById(R.id.favprogressbar)
        progressText=view.findViewById(R.id.favtxtnofavrestaurants)

        progressLayout.visibility=View.VISIBLE
        progressBar.visibility=View.VISIBLE
        progressText.visibility=View.INVISIBLE

        val foodlist=getFav(activity as Context).execute().get()

        if(foodlist.isEmpty())
        {
            progressBar.visibility=View.GONE
            progressText.visibility=View.VISIBLE

        }
        else
        {
            progressText.visibility=View.GONE
            progressLayout.visibility=View.GONE
            progressBar.visibility=View.GONE

            favAdapter= FavoritesAdapter(activity as Context,foodlist,userid)
            layoutManager= LinearLayoutManager(activity as Context)

            favrecyclerview.layoutManager=layoutManager
            favrecyclerview.adapter=favAdapter
        }













        return view
    }

}

    class getFav(val context: Context):AsyncTask<Void,Void,List<RestaurantEntity>>()
    {
        val db = Room.databaseBuilder(context,RestaurantDatabase::class.java,"favres-db").build()

        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
           val favlist= db.restaurantDao().getallRestaurant()
            db.close()
            return favlist
        }

    }
