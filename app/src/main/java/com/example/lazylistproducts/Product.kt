package com.example.lazylistproducts

data class ProductResponse(val products: List<Product>)

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val brand: String,
    val thumbnail: String
)
