package com.example.lazylistproducts


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import coil.compose.rememberImagePainter
import com.example.FetchProductsWorker
import com.example.Product
import com.example.ProductResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()

        setContent {
            ProductListScreen()
        }
    }
}



@Composable
fun ProductListScreen() {
    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)
    val workRequest = OneTimeWorkRequestBuilder<FetchProductsWorker>()
        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)  // ✅ Forces WorkManager to run immediately
        .build()
    val workInfo = workManager.getWorkInfoByIdLiveData(workRequest.id).observeAsState()

    val products = remember { mutableStateOf<List<Product>>(emptyList()) }

    LaunchedEffect(Unit) {
        Log.d("Proddd", "🚀 Enqueuing WorkManager request")
        workManager.enqueue(workRequest)
    }

    LaunchedEffect(workInfo.value) {
        if (workInfo.value?.state == WorkInfo.State.SUCCEEDED) {
            val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

            var productsJson: String? = null
            var attempts = 0

            // Retry reading SharedPreferences for up to 10 times
            while (attempts < 10) {
                productsJson = sharedPreferences.getString("products", null)

                if (productsJson != null) {
                    break
                }

                attempts++
                Thread.sleep(500) // Wait 500ms before trying again
            }

            if (productsJson == null) {
                Log.e("Proddd", "❌ SharedPreferences is still empty after retries!")
            } else {
                Log.d("Proddd", "📦 Products JSON from SharedPreferences: $productsJson")

                try {
                    val productListType = object : TypeToken<List<Product>>() {}.type
                    val productList: List<Product> = Gson().fromJson(productsJson, productListType)

                    products.value = productList
                    Log.d("Proddd", "✅ Loaded ${products.value.size} products from SharedPreferences")
                } catch (e: Exception) {
                    Log.e("ProductListScreen", "❌ Error parsing JSON: ${e.message}")
                }
            }
        }
    }

    Column {
        Text(text = "Product List", modifier = Modifier.padding(16.dp))

        LazyColumn {
            items(products.value) { product ->
                ProductItem(product)
            }
        }
    }
}


@Composable
fun ProductItem(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberImagePainter(data = product.thumbnail),
            contentDescription = product.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = product.title)
        Text(text = product.description)
        Text(text = "Price: $${product.price}")
    }
}


