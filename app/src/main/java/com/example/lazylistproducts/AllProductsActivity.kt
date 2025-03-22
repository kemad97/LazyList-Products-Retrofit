package com.example.lazylistproducts.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.lazylistproducts.DetailsActivity
import com.example.lazylistproducts.model.Product
import com.example.lazylistproducts.viewmodel.AllProductViewModel
import com.example.lazylistproducts.repo.ProductRepositoryImpl
import com.example.lazylistproducts.local.ProductDatabase
import com.example.lazylistproducts.remote.RetrofitInstance

class AllProductsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productDao = ProductDatabase.getDatabase(this).productDao()
        val apiService = RetrofitInstance.apiService
        val repository = ProductRepositoryImpl(productDao, apiService)
        val factory = AllProductViewModel.ProductViewModelFactory(repository)

        setContent {
            val viewModel: AllProductViewModel = viewModel(factory = factory)

            LaunchedEffect(Unit) {
                viewModel.fetchProductsFromApi()
            }

            val products = viewModel.products.observeAsState(emptyList())
            ProductListScreen(products.value, this,viewModel)

        }
    }
}

    @Composable
    fun ProductListScreen(products: List<Product>, myActivity: ComponentActivity , viewModel : AllProductViewModel) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(products) { product ->
                        ProductItem(product, myActivity,viewModel)
                    }
                }
            }
        }


    @Composable
    fun ProductItem(product: Product, myActivity: ComponentActivity , viewModel: AllProductViewModel) {
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
                    painter = rememberAsyncImagePainter(product.thumbnail),
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
                        text = product.title ?: "No Title",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Favorite Button
                    Button(
                        onClick = { viewModel.addToFavorites(product) }
                    ) {
                        Text(text = "Add to Favorites")
                    }
                }

            }
            }
        }






