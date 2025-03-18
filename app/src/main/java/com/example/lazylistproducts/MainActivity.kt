package com.example.lazylistproducts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image

import androidx.work.*

import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter

import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import java.io.IOException


class MainActivity : ComponentActivity() {

    var products = mutableStateOf<List<Product>>(emptyList())
    var loading = mutableStateOf(true)
    val productDao = ProductDatabase.getDatabase(applicationContext).productDao()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val workManager = WorkManager.getInstance(applicationContext)
        val workRequest = OneTimeWorkRequestBuilder<FetchProductsWorker>().build()

        workManager.enqueueUniqueWork(
            "FetchProductsWorker",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )

        lifecycleScope.launch(Dispatchers.IO) {
            products.value = productDao.getAllProducts()
        }

        workManager.getWorkInfoByIdLiveData(workRequest.id).observe(this) { workInfo ->
            if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val productDao = ProductDatabase.getDatabase(applicationContext).productDao()
                    products.value = productDao.getAllProducts()
                    loading.value = false
                }
            }
        }

        setContent {
            ProductListScreen(products.value, loading.value, this)
        }
    }
}

@Composable
fun ProductListScreen(products: List<Product>, loading: Boolean,myActivity: ComponentActivity) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(products) { product ->
                    ProductItem(product,myActivity)
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, myActivity: ComponentActivity) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick={
            val intent = Intent(myActivity, DetailsActivity::class.java).apply {
                putExtra("title", product.title)
                putExtra("description", product.description)
                putExtra("price", product.price)
                putExtra("brand", product.brand)
                putExtra("image", product.thumbnail)
            }
            myActivity.startActivity(intent)
        }

    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberImagePainter(product.thumbnail),
                contentDescription = product.title,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = product.title!!,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Spacer(modifier = Modifier.height(6.dp))


            }
        }
    }
}


