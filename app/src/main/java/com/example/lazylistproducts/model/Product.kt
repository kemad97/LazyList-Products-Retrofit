package com.example.lazylistproducts.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String? = "",
    val description: String? = "",
    val brand: String? = "",
    val price: Double? = 0.0,
    val thumbnail: String? = ""
)
