package com.jc.foodapp.adapter

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.jc.foodapp.model.FoodItem
import com.jc.foodapp.R
import com.jc.foodapp.database.FoodItemDatabase
import com.jc.foodapp.database.FoodItemEntity

class RestaurantMenuAdapter(val context: Context, val itemList:ArrayList<FoodItem>):RecyclerView.Adapter<DishListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_menu_single_rowitem, parent, false)
        return DishListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun onBindViewHolder(holder: DishListViewHolder, position: Int) {

        var item = itemList[position]
        var no = position + 1

        holder.txtno.text = "$no"
        holder.txtdishname.text = item.dishname
        holder.txtdishcost.text = "Rs.${item.dishcost}"

        val foodItemEntity = FoodItemEntity(
            item.dishid.toString(),
            item.dishname.toString(),
            item.dishcost.toString()
        )

        holder.btnadd.setOnClickListener {

            if (!cart(3, context, foodItemEntity).execute().get()) {
                val success = cart(1, context, foodItemEntity).execute().get()
                if (success) {
                    holder.btnadd.text = "Remove"
                    holder.btnadd.setTextColor(Color.WHITE)
                    holder.btnadd.setBackgroundResource(R.drawable.remove_button)
                    Toast.makeText(context,"${item.dishname} added to cart",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Some error Occured", Toast.LENGTH_SHORT).show()
                }
            } else{
                val fail = cart(2, context, foodItemEntity).execute().get()
                if (fail) {
                    holder.btnadd.text = "Add"
                    holder.btnadd.setTextColor(Color.BLACK)
                    holder.btnadd.setBackgroundResource(R.drawable.add_button)
                    Toast.makeText(context,"${item.dishname} removed from cart",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Some error Occured", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
    class DishListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtno: TextView = view.findViewById(R.id.txtno)
        var txtdishname: TextView = view.findViewById(R.id.txtdishname)
        var txtdishcost: TextView = view.findViewById(R.id.txtdishcost)
        var btnadd: Button = view.findViewById(R.id.btnadd)
    }

       class cart(val mode:Int,val context: Context,val foodItemEntity: FoodItemEntity):
           AsyncTask<Void, Void, Boolean>()
       {
           //resolved error after changing the name
             val db= Room.databaseBuilder(context,FoodItemDatabase::class.java,"fooditem-db").build()

           override fun doInBackground(vararg params: Void?): Boolean {
               //1-insert the entity to db
               //2-delete the entity to db
               //check if exists or not
        when(mode)
        {
            1->
            {
                db.foodItemDao().insertfoodItem(foodItemEntity)
                db.close()
                return true
            }
            2->
            {
                db.foodItemDao().deletefoodItem(foodItemEntity)
                db.close()
                return true
            }
            3->
            {
                val fooditem:FoodItemEntity=db.foodItemDao().getfoodItembyId(foodItemEntity.fooditemid.toString())
                db.close()
                return fooditem != null
            }
        }
        return false
    }



}