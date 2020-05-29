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
import com.jc.foodapp.database.RestaurantEntity
import com.squareup.picasso.Picasso

class FavoritesAdapter(val context: Context,val itemList:List<RestaurantEntity>,val userid:String):RecyclerView.Adapter<FavoritesAdapter.favViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_single_rowitem, parent, false)
        return favViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: favViewHolder, position: Int) {

        val item=itemList[position]

        Picasso.get().load(item.resimage).error(R.drawable.restaurant_default).into(holder.img)
        holder.name.text=item.resname
        holder.cost.text="Rs ${item.rescost}"
        holder.rating.text=item.resrating

        holder.rlayout.setOnClickListener {

            val intent = Intent(context, RestaurantMenuActivity::class.java)
            intent.putExtra("user_id",userid)
            intent.putExtra("res_id", item.resid)
            intent.putExtra("name", item.resname)
            intent.putExtra("image",item.resimage)
            context.startActivity(intent)
        }
    }

    class favViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        val img=view.findViewById<ImageView>(R.id.favimg)
        val name=view.findViewById<TextView>(R.id.favtxtresname)
        val cost=view.findViewById<TextView>(R.id.favtxtrescost)
        val rating=view.findViewById<TextView>(R.id.favtxtresrating)
        val rlayout:RelativeLayout=view.findViewById(R.id.rlayoutfav)

    }
}