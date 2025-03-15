package com.example.lazylistproducts

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson

class FetchProductsWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val productDao = ProductDatabase.getDatabase(applicationContext).productDao()
        return try {
            val response = RetrofitInstance.apiService.getProducts()
            val products = response.products

            if (products.isNotEmpty()) {
                productDao.insertProducts(products)
                Log.i("FetchProductsWorker", " Data successfully fetched and stored: ${products.size} products")

                // Return WorkManager success output
                val data = workDataOf("data" to Gson().toJson(products))
                return Result.success(data)
            } else {
                Log.e("FetchProductsWorker", "API returned empty product list")
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e("FetchProductsWorker", "Error fetching data", e)
            Result.failure()
        }
    }
}
