package com.example.lazylistproducts

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ProductResponse(val products: List<Product>)

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String? = "",
    val description: String? = "",
    val brand: String? = "",
    val price: Double? = 0.0,
    val thumbnail: String? = ""
)
