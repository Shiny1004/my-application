package com.jc.foodapp.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [FoodItemEntity::class],version = 1)
 abstract class FoodItemDatabase:RoomDatabase() {

     abstract fun foodItemDao():FoodItemDao
}