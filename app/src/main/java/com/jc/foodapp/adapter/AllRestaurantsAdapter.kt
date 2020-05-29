package com.jc.foodapp.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.jc.foodapp.R
import com.jc.foodapp.model.Restaurant
import com.jc.foodapp.activity.RestaurantMenuActivity
import com.jc.foodapp.database.RestaurantDatabase
import com.jc.foodapp.database.RestaurantEntity
import com.squareup.picasso.Picasso

class AllRestaurantsAdapter(val context: Context, val itemList:ArrayList<Restaurant>):RecyclerView.Adapter<AllRestaurantsAdapter.AllRestaurantsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllRestaurantsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.allrestaurants_single_rowitem, parent, false)
        return AllRestaurantsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: AllRestaurantsViewHolder, position: Int) {

        val item = itemList[position]

        holder.restaurantname.text = item.name
        holder.restaurantcost.text = "Rs. ${item.costforone}/person"
        holder.restaurantrating.text = item.rating
        Picasso.get().load(item.imageurl).error(R.drawable.restaurant_default)
            .into(holder.restaurantimage)

        val restaurantEntity = RestaurantEntity(
            item.resid.toString(),
            item.name.toString(),
            item.costforone.toString(),
            item.rating.toString(),
            item.imageurl.toString()
        )

        val checkfav = DBAsyncTask(context.applicationContext, restaurantEntity, 1).execute()
        val isFav = checkfav.get()
        if (isFav) {
            holder.restaurantfavorite.setImageResource(R.drawable.ic_icon_red_heart)
        } else {
            holder.restaurantfavorite.setImageResource(R.drawable.ic_icon_heart)
        }


        holder.restaurantfavorite.setOnClickListener {

            if (!DBAsyncTask(context.applicationContext, restaurantEntity, 1).execute().get()) {
                //this if condition is important
                val add = DBAsyncTask(context, restaurantEntity, 2).execute().get()

                if (add) {
                    holder.restaurantfavorite.setImageResource(R.drawable.ic_icon_red_heart)
                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Some error Occured", Toast.LENGTH_SHORT).show()
                }
            } else {
                val remove = DBAsyncTask(context, restaurantEntity, 3).execute().get()

                if (remove) {
                    holder.restaurantfavorite.setImageResource(R.drawable.ic_icon_heart)
                    Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Some error Occured", Toast.LENGTH_SHORT).show()
                }

            }

        }
        holder.restaurantrlayout.setOnClickListener {

            val intent = Intent(context, RestaurantMenuActivity::class.java)
            intent.putExtra("user_id",item.userid)
            intent.putExtra("res_id", item.resid)
            intent.putExtra("name", item.name)
            intent.putExtra("image",item.imageurl)
            context.startActivity(intent)
        }


    }

    class AllRestaurantsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restaurantname: TextView = view.findViewById(R.id.txtrestaurantname)
        val restaurantcost: TextView = view.findViewById(R.id.txtcost)
        val restaurantrating: TextView = view.findViewById(R.id.txtrating)
        val restaurantimage: ImageView = view.findViewById(R.id.imgrestaurant)
        val restaurantfavorite: ImageButton = view.findViewById(R.id.btnheart)
        val restaurantrlayout: LinearLayout = view.findViewById(R.id.llayoutallrestaurants)

    }
}

    class DBAsyncTask(val context: Context, val resEntity: RestaurantEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "favres-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {
                1 -> {
                    val res: RestaurantEntity =
                        db.restaurantDao().getresbyId(resEntity.resid.toString())
                    db.close()
                    return res != null
                }
                2 -> {
                    db.restaurantDao().insertRestaurant(resEntity)
                    db.close()
                    return true

                }
                3 -> {
                    db.restaurantDao().deleteRestaurant(resEntity)
                    db.close()
                    return true
                }

            }
            return false
        }

    }