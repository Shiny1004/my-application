package com.jc.foodapp.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.view.View.inflate
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.jc.foodapp.R
import com.jc.foodapp.adapter.SearchAdapter
import com.jc.foodapp.model.Restaurant
import com.jc.foodapp.util.ConnectionManager
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    lateinit var recylersearch: RecyclerView
    lateinit var recyclerAdapter: SearchAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var searchprogresslayout: RelativeLayout
    lateinit var searchprogresstext: TextView
    lateinit var edtxtsearch: EditText

    var searcharray = arrayListOf<Restaurant>()
    var displayarray = arrayListOf<Restaurant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val userid = this.arguments!!.getString("user_id", "00")

        recylersearch = view.findViewById(R.id.recyclersearch)
        searchprogresslayout = view.findViewById(R.id.searchprogresslayout)
        searchprogresstext = view.findViewById(R.id.searchprogresstext)
        edtxtsearch = view.findViewById(R.id.edtxtsearch)


        searchprogresslayout.visibility = View.INVISIBLE
        searchprogresslayout.visibility = View.INVISIBLE

        layoutManager = LinearLayoutManager(activity as Context)



        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            //no params

            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener {

                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")

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
                                searcharray.add(objectRestaurant)
                            }
                            displayarray.addAll(searcharray)
                            recyclerAdapter = SearchAdapter(activity as Context, displayarray)

                            recyclersearch.adapter = recyclerAdapter
                            recyclersearch.layoutManager = layoutManager

                        } else {
                            Toast.makeText(context, "Some error occured!", Toast.LENGTH_SHORT)
                                .show()

                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Some error occured!", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(context, "Some error occured!", Toast.LENGTH_SHORT).show()
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

        edtxtsearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                s.toString().toLowerCase(Locale.getDefault())

                if (s!!.isNotEmpty()) {
                    displayarray.clear()
                    val search = s!!.toString().toLowerCase(Locale.getDefault())

                    searcharray.forEach {

                        if (it.name.toLowerCase(Locale.getDefault()).contains(search)) {
                            displayarray.add(it)
                        }
                    }
                    if (displayarray.isEmpty()) {
                        searchprogresslayout.visibility = View.VISIBLE
                        searchprogresslayout.visibility = View.VISIBLE
                    } else {
                        searchprogresslayout.visibility = View.GONE
                        searchprogresslayout.visibility = View.GONE
                    }
                    recyclerAdapter.notifyDataSetChanged()
                } else {
                    searchprogresslayout.visibility = View.GONE
                    searchprogresslayout.visibility = View.GONE
                    displayarray.clear()
                    displayarray.addAll(searcharray)
                    recyclerAdapter.notifyDataSetChanged()
                }

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                s.toString().toLowerCase(Locale.getDefault())

                if (s!!.isNotEmpty()) {
                    displayarray.clear()
                    val search = s!!.toString().toLowerCase(Locale.getDefault())

                    searcharray.forEach {

                        if (it.name.toLowerCase(Locale.getDefault()).contains(search)) {
                            displayarray.add(it)
                        }
                    }
                    if (displayarray.isEmpty()) {
                        searchprogresslayout.visibility = View.VISIBLE
                        searchprogresslayout.visibility = View.VISIBLE
                    } else {
                        searchprogresslayout.visibility = View.GONE
                        searchprogresslayout.visibility = View.GONE
                    }
                    recyclerAdapter.notifyDataSetChanged()
                } else {
                    searchprogresslayout.visibility = View.GONE
                    searchprogresslayout.visibility = View.GONE
                    displayarray.clear()
                    displayarray.addAll(searcharray)
                    recyclerAdapter.notifyDataSetChanged()
                }

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s!!.isNotEmpty()) {
                    displayarray.clear()
                    val search = s!!.toString().toLowerCase()

                    searcharray.forEach {

                        if (it.name.toLowerCase().contains(search)) {
                            displayarray.add(it)
                        }
                    }
                    if (displayarray.isEmpty()) {
                        searchprogresslayout.visibility = View.VISIBLE
                        searchprogresslayout.visibility = View.VISIBLE
                    } else {
                        searchprogresslayout.visibility = View.GONE
                        searchprogresslayout.visibility = View.GONE
                    }
                    recyclerAdapter.notifyDataSetChanged()
                } else {
                    searchprogresslayout.visibility = View.GONE
                    searchprogresslayout.visibility = View.GONE
                    displayarray.clear()
                    displayarray.addAll(searcharray)
                    recyclerAdapter.notifyDataSetChanged()
                }
            }
        })
        return view
    }

}
