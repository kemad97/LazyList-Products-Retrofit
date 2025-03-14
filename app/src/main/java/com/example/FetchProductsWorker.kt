package com.example

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.RetrofitHelper
import com.google.gson.Gson
import retrofit2.Response

class FetchProductsWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val service = RetrofitHelper.api
        val sharedPreferences = applicationContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        return try {
            val response: Response<ProductResponse> = service.getProducts().execute()
            Log.d("FetchProductsWorker", "📢 Raw API Response: ${response.body()}")

            if (response.isSuccessful) {
                val products = response.body()?.products ?: emptyList()
                val productsJson = Gson().toJson(products)

                Log.d("FetchProductsWorker", "✅ Fetched ${products.size} products")

                // ✅ Store data in SharedPreferences
                sharedPreferences.edit().putString("products", productsJson).commit() // ✅ Forces immediate save

                return Result.success() // ✅ WorkManager will recognize success
            } else {
                Log.e("FetchProductsWorker", "❌ API request failed: ${response.errorBody()?.string()}")
                return Result.failure()
            }
        } catch (e: Exception) {
            Log.e("FetchProductsWorker", "❌ Exception: ${e.message}")
            return Result.retry()
        }
    }
}
