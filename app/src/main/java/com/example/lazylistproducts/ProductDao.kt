package com.example.lazylistproducts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)
}