package com.example.lazylistproducts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.lazylistproducts.local.ProductDatabase
import com.example.lazylistproducts.model.Product
import com.example.lazylistproducts.remote.RetrofitInstance.apiService
import com.example.lazylistproducts.repo.ProductRepositoryImpl
import com.example.lazylistproducts.ui.ProductItem
import com.example.lazylistproducts.viewmodel.FavoriteViewModel

class FavoriteProductsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productDao = ProductDatabase.getDatabase(this).productDao()
        val repository = ProductRepositoryImpl(productDao, apiService) // No API needed
        val factory = FavoriteViewModel.FavoritesViewModelFactory(repository)

        setContent {
            val viewModel: FavoriteViewModel = viewModel(factory = factory)
            val favoriteProducts by viewModel.favorites.collectAsStateWithLifecycle(emptyList())

            LaunchedEffect(Unit) {
                viewModel.getFavoriteProducts()
            }

                FavoriteProductsScreen(favoriteProducts, viewModel)
        }
    }
}

@Composable
fun FavoriteProductsScreen(favoriteProducts: List<Product>, viewModel: FavoriteViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Favorite Products", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(favoriteProducts) { product ->
                ProductItem(product, null, viewModel)
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, myActivity: ComponentActivity?, viewModel: FavoriteViewModel?) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                Text(text = product.title ?: "No Title", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(8.dp))

                if (viewModel != null) {
                    Button(onClick = { viewModel.removeFromFavorites(product) }) {
                        Text("Remove from Favorites")
                    }
                }
            }
        }
    }
}


