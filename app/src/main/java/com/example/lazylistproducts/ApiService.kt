package com.example.lazylistproducts

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("products")
   suspend fun getProducts(): ProductResponse
}
