package com.jc.foodapp.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jc.foodapp.model.Dish
import com.jc.foodapp.R
import com.jc.foodapp.model.order
import org.json.JSONArray

class OrderHistoryParentAdapter(val context: Context, val itemList:ArrayList<order>):RecyclerView.Adapter<OrderHistoryParentAdapter.orderHistoryViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderHistoryViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.orderhistory_single_rowitem,parent,false)
        return orderHistoryViewHolder(view)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: orderHistoryViewHolder, position: Int) {

        val i=itemList[position]

        val timestring = togettime(i.time)

        val rs="Total: Rs."
        holder.ohresname.text=i.resname
        holder.ohtotalcost.text="$rs ${i.totalcost}"
        holder.ohtime.text=timestring
        setup(holder.ohrecyclerviewchild,i)
    }
    class orderHistoryViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        val ohresname:TextView=view.findViewById(R.id.ohresname)
        val ohtotalcost:TextView=view.findViewById(R.id.ohtotalcost)
        val ohtime:TextView=view.findViewById(R.id.ohtime)
       val ohrecyclerviewchild:RecyclerView=view.findViewById(R.id.ohrecyclerviewchild)
    }

    private fun setup(ohrecyclerviewchild:RecyclerView,childList:order)
    {
        val fooditemlist=ArrayList<Dish>()
        for (i in 0 until childList.foodarray.length())
        {
            val fooditem=childList.foodarray.getJSONObject(i)
            fooditemlist.add(Dish( fooditem.getString("food_item_id"),
                fooditem.getString("name"),fooditem.getString("cost")))
        }
        val childlayoutManager=LinearLayoutManager(context)
        val childAdapter=OrderHistoryChildAdapter(context,fooditemlist)

        ohrecyclerviewchild.layoutManager=childlayoutManager
        ohrecyclerviewchild.itemAnimator=DefaultItemAnimator()
        ohrecyclerviewchild.adapter=childAdapter
    }
    fun togettime(s:String):String
    {
        var str=s.slice(0..5)
        val stra=s.slice(6..7)
        val st="${str}20${stra}"
        return st.replace('-','/',ignoreCase = false)
    }
}