package com.example.lazylistproducts.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lazylistproducts.FavoriteProductsActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainMenuScreen(this)
        }
    }
}

@Composable
fun MainMenuScreen(activity: ComponentActivity) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                val intent = Intent(activity, AllProductsActivity::class.java)
                activity.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("View All Products")
        }

        Button(
            onClick = {
                val intent = Intent(activity, FavoriteProductsActivity::class.java)
                activity.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("View Favorite Products")
        }

        Button(
            onClick = { activity.finish() }, // Exit App
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
        ) {
            Text("Exit App")
        }
    }
}
