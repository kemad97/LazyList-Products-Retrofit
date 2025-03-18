package com.example.lazylistproducts.repo

import com.example.lazylistproducts.local.ProductDao
import com.example.lazylistproducts.model.Product
import com.example.lazylistproducts.remote.ApiService

class ProductRepositoryImpl (
    private val productDao: ProductDao,
    private val apiService: ApiService
) : ProductsRepository{
    override suspend fun getProducts(): List<Product> {
        return productDao.getAllProductsFromDatabase()
    }

    override suspend fun insertProduct(product: Product) {
            return productDao.insertProducts(product)
    }

    override suspend fun deleteProduct(product: Product) {
    productDao.deleteProduct(product)
    }

    override suspend fun fetchProductsFromApi(): List<Product> {
        val response =apiService.getProducts()
        return response.products
    }

}