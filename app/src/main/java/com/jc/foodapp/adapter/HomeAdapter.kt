package com.jc.foodapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jc.foodapp.model.HomeRestaurant
import com.jc.foodapp.R
import com.jc.foodapp.activity.RestaurantMenuActivity
import com.squareup.picasso.Picasso

class HomeAdapter(val context: Context,val itemList:ArrayList<HomeRestaurant>) :RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.home_single_rowitem,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item=itemList[position]

        holder.homename.text=item.name
        holder.homerating.text="${item.rating}/5"

        Picasso.get().load(item.image).error(R.drawable.restaurant_default).into(holder.homeimage)

        holder.rlayout.setOnClickListener {

            val intent=Intent(context,RestaurantMenuActivity::class.java)
            intent.putExtra("user_id",item.userid)
            intent.putExtra("res_id",item.resid)
            intent.putExtra("name",item.name)
            intent.putExtra("image",item.image)
            context.startActivity(intent)
        }
    }
    class HomeViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        val homeimage:ImageView=view.findViewById(R.id.homeimgrestaurant)
        val homename:TextView=view.findViewById(R.id.hometxtname)
        val homerating:TextView=view.findViewById(R.id.hometxtrating)
        val rlayout:RelativeLayout=view.findViewById(R.id.rlayouthome)


    }
}