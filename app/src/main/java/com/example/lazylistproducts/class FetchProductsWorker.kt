package com.example.lazylistproducts

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson

class FetchProductsWorker(appContext: Context , workerParams: WorkerParameters) : CoroutineWorker( appContext, workerParams) {
    override suspend fun doWork(): Result {

        return try {
            val response=RetrofitInstance.apiService.getProducts()
            val data = workDataOf("data" to response.products.toString())
            Result.success(data)
        }catch (e:Exception)
        {
            Result.failure()
        }
    }


}