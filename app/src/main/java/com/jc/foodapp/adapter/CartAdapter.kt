package com.jc.foodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jc.foodapp.R
import com.jc.foodapp.database.FoodItemEntity

class CartAdapter(val context: Context,val itemList:List<FoodItemEntity>):RecyclerView.Adapter<CartAdapter.cartViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.cart_single_rowitem,parent,false)
        return cartViewHolder(view)

    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {

        val item=itemList[position]
        holder.cartfooditemname.text=item.fooditemname
        holder.cartfooditemcost.text="Rs. ${item.fooditemcost}"

    }
    class cartViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        val cartfooditemname:TextView=view.findViewById(R.id.cartfooditemname)
        val cartfooditemcost:TextView=view.findViewById(R.id.cartfooditemcost)
    }
}