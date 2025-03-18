package com.example.lazylistproducts.repo

import com.example.lazylistproducts.model.Product

interface ProductsRepository {
    suspend fun getProducts(): List<Product>
    suspend fun insertProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun fetchProductsFromApi(): List<Product>

}