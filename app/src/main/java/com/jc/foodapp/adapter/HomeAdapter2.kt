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
import com.jc.foodapp.R
import com.jc.foodapp.activity.RestaurantMenuActivity
import com.jc.foodapp.model.HomeRestaurant
import com.squareup.picasso.Picasso


class HomeAdapter2(val context:Context,val itemList1:ArrayList<HomeRestaurant>,val itemList2:ArrayList<String>):RecyclerView.Adapter<HomeAdapter2.home2ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): home2ViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.home2_single_rowitem,parent,false)
        return home2ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return itemList1.size
    }

    override fun onBindViewHolder(holder: home2ViewHolder, position: Int) {

        val item1=itemList1[position]
        val item2=itemList2[position]

        holder.txt.text=item2
        Picasso.get().load(item1.image).error(R.drawable.restaurant_default).into(holder.img)

        holder.rlayout1.setOnClickListener {
            val intent= Intent(context, RestaurantMenuActivity::class.java)
            intent.putExtra("user_id",item1.userid)
            intent.putExtra("res_id",item1.resid)
            intent.putExtra("name",item1.name)
            intent.putExtra("image",item1.image)
            context.startActivity(intent)
        }

    }
    class home2ViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        val txt:TextView=view.findViewById(R.id.home2txt1)
        val img: ImageView =view.findViewById(R.id.homeimg)
        val rlayout1:RelativeLayout=view.findViewById(R.id.home2rlayout)

    }
}