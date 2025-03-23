package com.example.lazylistproducts.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lazylistproducts.model.Product
import com.example.lazylistproducts.repo.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel  (private val repository: ProductsRepository ) : ViewModel()
{

    private val _mutableFavorites= MutableStateFlow<List<Product>>(emptyList())
    val favorites: StateFlow<List<Product>> = _mutableFavorites.asStateFlow()

    private val _mutableMessage = MutableStateFlow("")
    val message: StateFlow<String> = _mutableMessage.asStateFlow()

     fun getFavoriteProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteList = repository.getProducts()
                .catch { e -> _mutableMessage.value = "Error loading favorites: ${e.message}" }
            favoriteList.collect {
                _mutableFavorites.value = it
                Log.i("ViewModel", "Fetched from Database: $favoriteList")
            }
        }
    }

    fun removeFromFavorites(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteProduct(product)
              //  getFavoriteProducts() // Refresh favorites list
                _mutableMessage.value=("Removed from favorites")
            } catch (e: Exception) {
                _mutableMessage.value=("Failed to remove product: ${e.message}")
            }
        }
    }

    class FavoritesViewModelFactory(private val repository: ProductsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(repository ) as T
        }

    }


}



