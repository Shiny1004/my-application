package com.jc.foodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jc.foodapp.model.Dish
import com.jc.foodapp.R

class OrderHistoryChildAdapter(val context: Context,val list:ArrayList<Dish>):RecyclerView.Adapter<OrderHistoryChildAdapter.childViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): childViewHolder {
        val view=LayoutInflater.from(parent.context).
            inflate(R.layout.orderhistory_fooditem_single_rowitem,parent,false)
        return childViewHolder(view)
    }
    override fun getItemCount(): Int {
       return list.size
    }
    override fun onBindViewHolder(holder: childViewHolder, position: Int) {

        val ite=list[position]

        holder.dishname.text=ite.dishname
        holder.dishcost.text="Rs. ${ite.dishcost}"
    }
    class childViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        val dishname:TextView=view.findViewById(R.id.orderhistoryfooditemname)
        val dishcost:TextView=itemView.findViewById(R.id.orderhistoryfooditemcost)
    }
}