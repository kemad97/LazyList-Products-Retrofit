package com.example.lazylistproducts.repo

import com.example.lazylistproducts.local.ProductDao
import com.example.lazylistproducts.model.Product
import com.example.lazylistproducts.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepositoryImpl  (
    private val productDao: ProductDao,
    private val apiService: ApiService
) : ProductsRepository {

    override fun getProducts(): Flow<List<Product>> {
        return productDao.getAllProductsFromDatabase()
    }

    override suspend fun insertProduct(product: Product) {
        return productDao.insertProducts(product)
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    override fun fetchProductsFromApi(): Flow<List<Product>> {
        return flow {
            val response = apiService.getProducts()
            emit(response.products)
        }
    }
}