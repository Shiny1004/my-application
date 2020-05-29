package com.jc.foodapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jc.foodapp.R
import com.jc.foodapp.activity.RestaurantMenuActivity
import com.jc.foodapp.model.Restaurant
import com.squareup.picasso.Picasso
import java.util.ArrayList

class SearchAdapter(val context: Context, var itemList: ArrayList<Restaurant>) : RecyclerView.Adapter<SearchAdapter.searchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): searchViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.search_single_rowitem,parent,false)
        return searchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: searchViewHolder, position: Int) {

        val item = itemList[position]
        holder.restaurantname.text = item.name
        holder.restaurantcost.text = "Rs. ${item.costforone}/person"
        holder.restaurantrating.text = item.rating
        Picasso.get().load(item.imageurl).error(R.drawable.restaurant_default)
            .into(holder.restaurantimage)

        holder.restaurantllayout.setOnClickListener {

            val intent = Intent(context, RestaurantMenuActivity::class.java)
            intent.putExtra("user_id",item.userid)
            intent.putExtra("res_id", item.resid)
            intent.putExtra("name", item.name)
            intent.putExtra("image",item.imageurl)
            context.startActivity(intent)
        }

    }
    class searchViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val restaurantname: TextView = view.findViewById(R.id.searchtxtrestaurantname)
        val restaurantcost: TextView = view.findViewById(R.id.searchtxtcost)
        val restaurantrating: TextView = view.findViewById(R.id.searchtxtrating)
        val restaurantimage: ImageView = view.findViewById(R.id.imgsearch)
        val restaurantllayout: LinearLayout = view.findViewById(R.id.llayoutsearch)
    }

}

