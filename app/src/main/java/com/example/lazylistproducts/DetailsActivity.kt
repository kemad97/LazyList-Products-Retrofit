package com.example.lazylistproducts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra("title") ?: "Unknown"
        val description = intent.getStringExtra("description") ?: "No description available"
        val price = intent.getDoubleExtra("price", 0.0)
        val brand = intent.getStringExtra("brand") ?: "Unknown"
        val image = intent.getStringExtra("image") ?: ""

        setContent {
            ProductDetailsScreen(title, description, price, brand, image)
        }
    }
}

@Composable
fun ProductDetailsScreen(title: String, description: String, price: Double, brand: String, image: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberImagePainter(image),
            contentDescription = title,
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Brand: $brand", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Price: $${price}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = description, style = MaterialTheme.typography.bodyMedium)
    }
}
