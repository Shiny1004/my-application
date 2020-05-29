package com.jc.foodapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface RestaurantDao {
    @Insert
    fun insertRestaurant(restaurantEntity: RestaurantEntity)

    @Delete
    fun deleteRestaurant(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM Favorites")
    fun getallRestaurant():List<RestaurantEntity>

    @Query("SELECT * FROM Favorites WHERE resid=:res_id")
    fun getresbyId(res_id:String):RestaurantEntity
}