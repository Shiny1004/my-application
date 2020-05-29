package com.jc.foodapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CartItems")
data class  FoodItemEntity (

    @PrimaryKey val fooditemid:String,
    @ColumnInfo(name = "fooditem_name") val fooditemname:String,
    @ColumnInfo(name = "fooditem_cost")  val fooditemcost:String
)