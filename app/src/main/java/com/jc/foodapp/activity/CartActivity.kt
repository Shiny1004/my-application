package com.jc.foodapp.activity

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationBuilderWithBuilderAccessor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jc.foodapp.R
import com.jc.foodapp.adapter.CartAdapter
import com.jc.foodapp.database.FoodItemDatabase
import com.jc.foodapp.database.FoodItemEntity
import com.jc.foodapp.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

class CartActivity : AppCompatActivity() {

    lateinit var cartresname:TextView
    lateinit var btnplaceorder:Button
    lateinit var carttotalcost:TextView
    lateinit var carttoolbar:Toolbar
    lateinit var recyclercart:RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: CartAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartresname=findViewById(R.id.cartresname)
        btnplaceorder=findViewById(R.id.btnplaceorder)
        carttotalcost=findViewById(R.id.carttotalcost)
        carttoolbar=findViewById(R.id.carttoolbar)
        recyclercart=findViewById(R.id.recyclercart)

        layoutManager=LinearLayoutManager(applicationContext)

        val fooditemarray=getcartItems(this).execute().get()

        setSupportActionBar(carttoolbar)
        supportActionBar?.title="Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(intent!=null) {

            val userid=intent.getStringExtra("user_id")
            val resname=intent.getStringExtra("res_name")
            val resid=intent.getStringExtra("res_id")

            var totalcost:Int=0

            for(i in 0 until fooditemarray.size)
            {
                val item=fooditemarray[i]
                totalcost +=item.fooditemcost.toInt()
            }

            cartresname.text=resname
            carttotalcost.text="Total: Rs. ${totalcost}"


            recyclerAdapter = CartAdapter(applicationContext
                ,fooditemarray)
            recyclercart.adapter = recyclerAdapter
            recyclercart.layoutManager = layoutManager


            btnplaceorder.setOnClickListener {

                val queue=Volley.newRequestQueue(this)
                val url="http://13.235.250.119/v2/place_order/fetch_result/"

                val ja=JSONArray()

                for(i in 0 until fooditemarray.size)
                {
                    val jo=JSONObject()//object must be created every time else same item is sent
                    //to the array for every iteration
                    val item=fooditemarray[i]
                    ja.put(i,jo.put("food_item_id",item.fooditemid))
                }

                val jsonParams=JSONObject()
                jsonParams.put("user_id",userid)
                jsonParams.put("restaurant_id",resid)
                jsonParams.put("total_cost",totalcost.toString())
                jsonParams.put("food",ja)//request was successful but no data appeared in order history fragment only because toString() was
                //used along with ja-jsonarray


                if (ConnectionManager().checkConnectivity(this))
                {
                    val jsonObjectRequest=object :JsonObjectRequest(Request.Method.POST,url,jsonParams,

                        Response.Listener {
                            try {
                                val data=it.getJSONObject("data")
                                val success=data.getBoolean("success")
                                if(success)
                                {
                                    val intent=Intent(this,OrderPlacedActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                }
                                else
                                {
                                    Toast.makeText(
                                        this,
                                        "Some Error Occured.Try again later", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            catch (e:Exception)
                            {
                                Toast.makeText(
                                    this,
                                    "Some Error Occured.Try again later", Toast.LENGTH_SHORT
                                ).show()
                            }

                    },Response.ErrorListener {
                            Toast.makeText(
                                this,
                                "Some Error Occured.Try again later", Toast.LENGTH_SHORT
                            ).show()
                    })
                    {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "b9e05b8368cd66"
                            return headers
                        }
                    }
                    queue.add(jsonObjectRequest)
                    }
                else
                {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("ERROR")
                    dialog.setMessage("No Internet Connection")
                    dialog.setPositiveButton("Open Settings") { text, listener ->
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                    } //will navigate to settings
                    dialog.setNegativeButton("Exit") { text, listener ->
                        ActivityCompat.finishAffinity(this)
                    }//closes the app
                    dialog.create()
                    dialog.show()
                }
            }

        }

    }
    class getcartItems(val context:Context):AsyncTask<Void,Void,List<FoodItemEntity>>() {

        val db = Room.databaseBuilder(context,FoodItemDatabase::class.java,"fooditem-db").build()

        override fun doInBackground(vararg params: Void?):List<FoodItemEntity> {

           val r= db.foodItemDao().getallfoodItems()
            db.close()
            return r
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item.itemId
        if(id==android.R.id.home)
        {
            finish()
        }
            return super.onOptionsItemSelected(item)
    }


}
