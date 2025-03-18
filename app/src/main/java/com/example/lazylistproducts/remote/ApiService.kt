package com.example.lazylistproducts.remote

import com.example.lazylistproducts.remote.ProductResponse
import retrofit2.http.GET

interface ApiService {
    @GET("products")
   suspend fun getProducts(): ProductResponse
}
