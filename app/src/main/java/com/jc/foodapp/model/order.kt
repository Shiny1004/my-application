package com.jc.foodapp.model

import org.json.JSONArray

data class order (

    val orderid:String,
    val resname:String,
    val totalcost:String,
    val time:String,
    val foodarray:JSONArray
)