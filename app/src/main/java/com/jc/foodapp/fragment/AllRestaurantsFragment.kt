package com.jc.foodapp.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs

import com.jc.foodapp.R
import com.jc.foodapp.model.Restaurant
import com.jc.foodapp.adapter.AllRestaurantsAdapter
import com.jc.foodapp.util.ConnectionManager
import kotlinx.android.synthetic.main.fragment_all_restaurants.*
import java.util.*
import kotlin.Comparator


class AllRestaurantsFragment : Fragment() {

    lateinit var recyclerallrestaurants: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: AllRestaurantsAdapter
    lateinit var allresprogresslayout:RelativeLayout
    lateinit var allresprogressbar:ProgressBar
    lateinit var allrestxterroroccured:TextView
    lateinit var sharedPreferences: SharedPreferences

    //Updating list according to rating
    var ratingComparator=Comparator<Restaurant>{res1,res2->

        if(res1.rating.compareTo(res2.rating)==0)
        {
            res1.name.compareTo(res2.name)
        }
        else
        {
            res1.rating.compareTo(res2.rating)
        }
    }

    //Updating list according to costforone
    var costforoneComparator=Comparator<Restaurant>{res1,res2->

        if(res1.costforone.compareTo(res2.costforone)==0)
        {
            res1.name.compareTo(res2.name)
        }
        else
        {
            res1.costforone.compareTo(res2.costforone)
        }
    }

    //sorting according to alphabetical order
    var nameComparator=Comparator<Restaurant>{res1,res2->

        res1.name.compareTo(res2.name)
    }

    val restaurantsarray = arrayListOf<Restaurant>()//empty array

    val a= arrayOf("Rating (High to Low)","Rating (Low to High)","Cost per person(High to Low)","Cost per person(Low to High)","Alphabetical Order")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_all_restaurants, container, false)
        val userid=this.arguments!!.getString("user_id","00")


        sharedPreferences= activity!!.getSharedPreferences(getString(R.string.preferences_check),Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("check").apply()


        recyclerallrestaurants = view.findViewById(R.id.recyclerallrestaurants)
        allresprogresslayout=view.findViewById(R.id.allresprogresslayout)
        allresprogressbar=view.findViewById(R.id.allresprogressbar)
        allrestxterroroccured=view.findViewById(R.id.allreserroroccured)
        layoutManager = LinearLayoutManager(activity)

        allresprogresslayout.visibility=View.VISIBLE
        allresprogressbar.visibility=View.VISIBLE
        allrestxterroroccured.visibility=View.GONE

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            //no params

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {

                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")

                        allresprogresslayout.visibility=View.GONE
                        allresprogressbar.visibility=View.GONE

                        if (success) {

                            val dat = data.getJSONArray("data")
                            for (i in 0 until dat.length()) {
                                val jsonObjectRestaurant = dat.getJSONObject(i)
                                val objectRestaurant =
                                    Restaurant(
                                        userid,
                                        jsonObjectRestaurant.getString("id"),
                                        jsonObjectRestaurant.getString("name"),
                                        jsonObjectRestaurant.getString("rating"),
                                        jsonObjectRestaurant.getString("cost_for_one"),
                                        jsonObjectRestaurant.getString("image_url")
                                    )
                                restaurantsarray.add(objectRestaurant)
                            }

                            if(activity != null) {
                                recyclerAdapter =
                                    AllRestaurantsAdapter(activity as Context, restaurantsarray)

                                recyclerallrestaurants.adapter = recyclerAdapter
                                recyclerallrestaurants.layoutManager = layoutManager
                            }

                        } else {
                            allresprogresslayout.visibility=View.VISIBLE
                            allresprogressbar.visibility=View.GONE
                            allrestxterroroccured.visibility=View.VISIBLE

                        }
                    } catch (e: Exception) {
                        allresprogresslayout.visibility=View.VISIBLE
                        allresprogressbar.visibility=View.GONE
                        allrestxterroroccured.visibility=View.VISIBLE
                    }
                },
                Response.ErrorListener {
                    allresprogresslayout.visibility=View.VISIBLE
                    allresprogressbar.visibility=View.GONE
                    allrestxterroroccured.visibility=View.VISIBLE
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "b9e05b8368cd66"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        } else {

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("ERROR")
            dialog.setMessage("No Internet Connection")
            dialog.setPositiveButton("Open Settings") { text, Listener ->

                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
            }
            dialog.setNegativeButton("Exit") { text, Listener ->

                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==R.id.txtsort)
        {
            var j:Int=-1
            val d= AlertDialog.Builder(activity as Context)
            d.setIcon(R.drawable.ic_icon_sort)
            d.setTitle("Sort by")
            val i=sharedPreferences.getInt("check",-1)

            d.setSingleChoiceItems(a,i) { di, which ->
                j=which
            }
            d.setPositiveButton("Apply"){text,listener->
                when(j)
                {
                    0->
                    {
                        Collections.sort(restaurantsarray,ratingComparator)
                        //will sort in increasing order since we need H to L
                        restaurantsarray.reverse()
                        //so we reverse the list again
                    }
                    1-> Collections.sort(restaurantsarray,ratingComparator)
                    2->
                    {
                        Collections.sort(restaurantsarray,costforoneComparator)
                        restaurantsarray.reverse()
                    }
                    3-> Collections.sort(restaurantsarray,costforoneComparator)
                    4-> Collections.sort(restaurantsarray, nameComparator)
                }
                sharedPreferences.edit().putInt("check",j).apply()
                recyclerAdapter.notifyDataSetChanged()
            }
            d.setNegativeButton("Cancel"){text,listener->
                //do nothing
            }
            d.create()
            d.show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity!!.menuInflater.inflate(R.menu.menu_sort,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}
