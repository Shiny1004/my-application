package com.jc.foodapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorites")
data class RestaurantEntity (

    @PrimaryKey val resid:String,
    @ColumnInfo(name = "res_name") val resname:String,
    @ColumnInfo(name = "res_cost") val rescost:String,
    @ColumnInfo(name = "res_rating") val resrating:String,
    @ColumnInfo(name = "res_image") val resimage:String

)