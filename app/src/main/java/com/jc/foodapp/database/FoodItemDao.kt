package com.jc.foodapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodItemDao {

    @Insert
    fun insertfoodItem(foodItemEntity: FoodItemEntity)

    @Delete
    fun deletefoodItem(foodItemEntity: FoodItemEntity)

    @Query("SELECT * FROM CartItems WHERE fooditemid=:fooditem_id")
    fun getfoodItembyId(fooditem_id:String):FoodItemEntity

    @Query("SELECT * FROM CartItems")
    fun getallfoodItems():List<FoodItemEntity>

    @Query("SELECT count(*) FROM CartItems")
    fun getFoodItemscount():Int

    @Query("DELETE FROM CartItems")
    fun deleteAll()
}