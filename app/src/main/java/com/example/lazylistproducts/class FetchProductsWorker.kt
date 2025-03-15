package com.example.lazylistproducts

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson

class FetchProductsWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val productDao = ProductDatabase.getDatabase(applicationContext).productDao()

        if (!NetworkUtils.isNetworkAvailable(applicationContext)) {
            Log.i("FetchProductsWorker", "No internet connection. Using cached data.")
            val cachedProducts = productDao.getAllProducts()
            return Result.success(workDataOf("data" to Gson().toJson(cachedProducts)))

        }

        return try {
            val response = RetrofitInstance.apiService.getProducts()
            val products = response.products

            if (products.isEmpty()) {
                Log.e("FetchProductsWorker", "API returned empty list")
                return Result.failure()
            }

            productDao.insertProducts(products)
            Log.i("FetchProductsWorker", "Data fetched and stored: ${products.size} products")

            Result.success(workDataOf("data" to Gson().toJson(products)))
        } catch (e: Exception) {
            Log.e("FetchProductsWorker", "Error fetching data", e)
            Result.retry()
        }
    }
}
