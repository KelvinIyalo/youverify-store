package com.kelviniyalo.youverifystorekelviniyalo.data.local_data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product

@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao

}