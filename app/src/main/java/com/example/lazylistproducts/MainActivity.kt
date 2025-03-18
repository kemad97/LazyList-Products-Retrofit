package com.example.lazylistproducts

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter

import androidx.work.WorkManager
import com.example.lazylistproducts.local.ProductDatabase
import com.example.lazylistproducts.model.Product
import com.example.lazylistproducts.remote.ApiService
import com.example.lazylistproducts.remote.RetrofitInstance
import com.example.lazylistproducts.repo.ProductsRepository
import com.example.lazylistproducts.viewmodel.AllProductViewModel
import com.example.lazylistproducts.viewmodel.FavoriteViewModel
import kotlinx.coroutines.Dispatchers

import com.example.lazylistproducts.repo.ProductRepositoryImpl



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productDao = ProductDatabase.getDatabase(this).productDao()
        val apiService = RetrofitInstance.apiService
        val repository = ProductRepositoryImpl(productDao, apiService)

        val factory = AllProductViewModel.ProductViewModelFactory(repository)
        val factory2 = FavoriteViewModel.FavoritesViewModelFactory(repository)



        setContent {
            val allProductViewModel: AllProductViewModel = viewModel(factory = factory)
            val favoriteViewModel: FavoriteViewModel = viewModel(factory = factory2)

            allProductViewModel.fetchProductsFromApi()


            favoriteViewModel.getFavoriteProducts()





        }
    }
}

@Composable
fun ProductListScreen(products: List<Product>, loading: Boolean, myActivity: ComponentActivity) {
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


