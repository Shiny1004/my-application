package com.jc.foodapp.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jc.foodapp.model.FoodItem
import com.jc.foodapp.R
import com.jc.foodapp.adapter.RestaurantMenuAdapter
import com.jc.foodapp.database.FoodItemDatabase
import com.jc.foodapp.util.ConnectionManager
import com.squareup.picasso.Picasso
import java.util.*

class RestaurantMenuActivity : AppCompatActivity() {

    val arraydish= arrayListOf<FoodItem>()
    lateinit var recyclerviewResMenu:RecyclerView
    lateinit var recyclerAdapter:RestaurantMenuAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var btnproceedtocart:Button
    lateinit var restoolbar:Toolbar
    lateinit var imgresmenu:ImageView
    lateinit var txtresname:TextView
    lateinit var resmenuprogresslayout:RelativeLayout
    lateinit var resmenuprogressbar:ProgressBar
    lateinit var resmenutxterroroccured:TextView


    lateinit var mainHandler: Handler//to check the cart every sec if not empty button appears

    private val updateTextTask = object : Runnable {
        override fun run() {
            check()
            mainHandler.postDelayed(this, 500)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)//to hide the status bar
        setContentView(R.layout.activity_restaurant_menu)

        recyclerviewResMenu=findViewById(R.id.menurecyclerview)

        restoolbar=findViewById(R.id.restoolbar)
        imgresmenu=findViewById(R.id.imgresmenu)
        txtresname=findViewById(R.id.txtresname)
        layoutManager=LinearLayoutManager(this@RestaurantMenuActivity)
        btnproceedtocart=findViewById(R.id.btnproceedtocart)

        resmenuprogresslayout=findViewById(R.id.resmenuprogresslayout)
        resmenuprogressbar=findViewById(R.id.resmenuprogressbar)
        resmenutxterroroccured=findViewById(R.id.resmenutxterroroccured)

        resmenuprogresslayout.visibility=View.VISIBLE
        resmenuprogressbar.visibility=View.VISIBLE
        resmenutxterroroccured.visibility=View.INVISIBLE


        btnproceedtocart.visibility=View.GONE

        checkcart(this,2).execute().get()


        if (intent != null) {
            val userid=intent.getStringExtra("user_id")
            val resid = intent.getStringExtra("res_id")
            val name = intent.getStringExtra("name")
            val imageurl=intent.getStringExtra("image")
            val oldurl = "http://13.235.250.119/v2/restaurants/fetch_result/"

           setSupportActionBar(restoolbar)
            val img=Picasso.get().load(imageurl).error(R.drawable.restaurant_default).into(imgresmenu)
            txtresname.text=name

            mainHandler = Handler(Looper.getMainLooper())

            btnproceedtocart.setOnClickListener {

                    val intent = Intent(this, CartActivity::class.java)
                    intent.putExtra("user_id", userid)
                    intent.putExtra("res_name",name)
                    intent.putExtra("res_id", resid)
                    startActivity(intent)

            }
            supportActionBar?.title=""
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            if (ConnectionManager().checkConnectivity(this@RestaurantMenuActivity)) {

                val queue = Volley.newRequestQueue(this@RestaurantMenuActivity)
                val url = "$oldurl$resid"

                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener {

                        try {
                            val data=it.getJSONObject("data")
                            val success=data.getBoolean("success")

                            resmenuprogresslayout.visibility=View.GONE
                            resmenuprogressbar.visibility=View.GONE
                            if(success)
                            {
                                val jsonobjectdish=data.getJSONArray("data")

                                for(i in 0 until jsonobjectdish.length())
                                {
                                    val dish=jsonobjectdish.getJSONObject(i)
                                    val d= FoodItem(
                                        userid,
                                        dish.getString("id"),
                                        dish.getString("name"),
                                        dish.getString("cost_for_one"),
                                        dish.getString("restaurant_id")
                                    )
                                    arraydish.add(d)
                                }
                                recyclerAdapter=RestaurantMenuAdapter(this,arraydish)

                               recyclerviewResMenu.adapter=recyclerAdapter
                                recyclerviewResMenu.layoutManager=layoutManager


                            }
                            else
                            {
                                resmenuprogresslayout.visibility=View.VISIBLE
                                resmenuprogressbar.visibility=View.GONE
                                resmenutxterroroccured.visibility=View.VISIBLE
                            }
                        }
                        catch (e:Exception)
                        {
                            resmenuprogresslayout.visibility=View.VISIBLE
                            resmenuprogressbar.visibility=View.GONE
                            resmenutxterroroccured.visibility=View.VISIBLE
                        }
                    },
                    Response.ErrorListener {
                        resmenuprogresslayout.visibility=View.VISIBLE
                        resmenuprogressbar.visibility=View.GONE
                        resmenutxterroroccured.visibility=View.VISIBLE
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
            } else {

                val dialog = AlertDialog.Builder(this@RestaurantMenuActivity)
                dialog.setTitle("ERROR")
                dialog.setMessage("No Internet Connection")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                } //will navigate to settings
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@RestaurantMenuActivity)
                }//closes the app
                dialog.create()
                dialog.show()
            }
        }
        else
        {
            Toast.makeText(this@RestaurantMenuActivity,"Some error Occurred",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item.itemId
        if(id==android.R.id.home)
        {

            if(checkcart(this,2).execute().get()==1) {
                finish() //closes the current activity so we return back to previous fragment
            }

        }
        return super.onOptionsItemSelected(item)
    }

    class checkcart(val context:Context,val mode:Int):AsyncTask<Void,Void,Int>()
    {
        val db=
            Room.databaseBuilder(context,FoodItemDatabase::class.java,"fooditem-db").build()
        override fun doInBackground(vararg params: Void?): Int {

            when(mode) {

                1->
                {
                    val count = db.foodItemDao().getFoodItemscount()
                    db.close()
                    return count
                }
                2->
                {
                    db.foodItemDao().deleteAll()
                    db.close()
                    return 1
                }
            }
            return 0
        }
    }

    fun check()
    {
        if (checkcart(this, 1).execute().get() != 0) {
            btnproceedtocart.isEnabled = true
            btnproceedtocart.visibility = View.VISIBLE
        } else {
            btnproceedtocart.isEnabled = false
            btnproceedtocart.visibility = View.INVISIBLE
        }
    }

    override fun onPause() {
        mainHandler.removeCallbacks(updateTextTask)

        super.onPause()
    }

    override fun onResume() {
        mainHandler.post(updateTextTask)
        super.onResume()
    }


}
