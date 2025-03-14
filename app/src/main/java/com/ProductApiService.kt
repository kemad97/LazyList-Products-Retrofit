package com

import com.example.Product
import com.example.ProductResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ProductApiService {
    @GET("products?limit=5")
    fun getProducts(): Call<ProductResponse>

}

object  RetrofitHelper {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ProductApiService = retrofit.create(ProductApiService::class.java)


}

