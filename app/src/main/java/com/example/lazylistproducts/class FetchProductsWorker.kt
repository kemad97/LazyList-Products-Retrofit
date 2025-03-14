package com.example.lazylistproducts

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson

class FetchProductsWorker(context: Context , workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val apiService = RetrofitInstance.apiService

        return try {
            val response = apiService.getProducts().execute()
            if (response.isSuccessful) {
                val productsJson = Gson().toJson(response.body()?.products ?: emptyList<Product>())
                Result.success(workDataOf("data" to productsJson))
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }
}