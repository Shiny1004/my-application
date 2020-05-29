package com.jc.foodapp.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jc.foodapp.model.Dish

import com.jc.foodapp.R
import com.jc.foodapp.adapter.OrderHistoryParentAdapter
import com.jc.foodapp.model.order
import com.jc.foodapp.util.ConnectionManager
import java.lang.Exception
import java.util.HashMap

class OrderHistoryFragment : Fragment() {

    lateinit var ohrecyclerviewparent:RecyclerView
    lateinit var recyclerAdapter:OrderHistoryParentAdapter
    lateinit var ohprogresslayout: RelativeLayout
    lateinit var ohprogressbar: ProgressBar
    lateinit var ohtxterroroccured:TextView
    lateinit var ohtxtnoorders:TextView

    val orderarray= arrayListOf<order>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_order_history, container, false)

        val userid = this.arguments!!.getString("user_id")

        ohrecyclerviewparent=view.findViewById(R.id.ohrecyclerviewparent)
        ohprogresslayout=view.findViewById(R.id.ohprogresslayout)
        ohprogressbar=view.findViewById(R.id.ohprogressbar)
        ohtxterroroccured=view.findViewById(R.id.ohtxterroroccured)
        ohtxtnoorders=view.findViewById(R.id.ohtxtnoorders)

        ohprogresslayout.visibility=View.VISIBLE
        ohprogressbar.visibility=View.VISIBLE
        ohtxterroroccured.visibility=View.INVISIBLE
        ohtxtnoorders.visibility=View.INVISIBLE


        val queue=Volley.newRequestQueue(context)
        val oldurl="http://13.235.250.119/v2/orders/fetch_result/"
        val url="$oldurl$userid"

        if (ConnectionManager().checkConnectivity(activity as Context))
            {
                val jsonObjectRequest=object : JsonObjectRequest(Request.Method.GET,url,null,
                    Response.Listener {

                        try {
                            val dat = it.getJSONObject("data")
                            val success = dat.getBoolean("success")

                            ohprogresslayout.visibility=View.GONE
                            ohprogressbar.visibility=View.GONE
                            ohtxterroroccured.visibility=View.GONE
                            ohtxtnoorders.visibility=View.GONE

                            if (success) {

                                val data = dat.getJSONArray("data")
                                for (i in 0 until data.length()) {

                                    val item = data.getJSONObject(i)

                                    orderarray.add(
                                        order(item.getString("order_id"),
                                            item.getString("restaurant_name"),
                                            item.getString("total_cost"),
                                            item.getString("order_placed_at"),
                                            item.getJSONArray("food_items"))
                                    )
                                }
                                if(orderarray.isEmpty())
                                {
                                    ohprogresslayout.visibility=View.VISIBLE
                                    ohtxtnoorders.visibility=View.VISIBLE
                                    ohprogressbar.visibility=View.GONE
                                    ohtxterroroccured.visibility=View.GONE
                                }

                                if(activity != null) {
                                    recyclerAdapter =
                                        OrderHistoryParentAdapter(activity as Context, orderarray)

                                    val layoutManager = LinearLayoutManager(activity as Context)

                                    ohrecyclerviewparent.layoutManager = layoutManager
                                    ohrecyclerviewparent.itemAnimator = DefaultItemAnimator()
                                    ohrecyclerviewparent.adapter = recyclerAdapter
                                }
                            }
                            else {
                                ohprogresslayout.visibility=View.VISIBLE
                                ohprogressbar.visibility=View.GONE
                                ohtxterroroccured.visibility=View.VISIBLE
                                ohtxtnoorders.visibility=View.GONE
                            }
                        }
                        catch (e:Exception)
                        {
                            ohprogresslayout.visibility=View.VISIBLE
                            ohprogressbar.visibility=View.GONE
                            ohtxterroroccured.visibility=View.VISIBLE
                            ohtxtnoorders.visibility=View.GONE
                        }
                    },
                    Response.ErrorListener {
                        ohprogresslayout.visibility=View.VISIBLE
                        ohprogressbar.visibility=View.GONE
                        ohtxterroroccured.visibility=View.VISIBLE
                        ohtxtnoorders.visibility=View.GONE
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
        else{
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

    }
