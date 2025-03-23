package com.example.lazylistproducts.repo

import com.example.lazylistproducts.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun insertProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    //suspend fun fetchProductsFromApi(): List<Product>
    fun fetchProductsFromApi(): Flow<List<Product>>

}