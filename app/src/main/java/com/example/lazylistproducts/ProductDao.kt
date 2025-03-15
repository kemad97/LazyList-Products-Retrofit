package com.example.lazylistproducts

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import retrofit2.http.Query

interface ProductDao {

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)
}