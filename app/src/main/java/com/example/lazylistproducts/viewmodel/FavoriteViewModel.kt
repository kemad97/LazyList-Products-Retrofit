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
import kotlinx.coroutines.launch

class FavoriteViewModel  (private val repository: ProductsRepository ) : ViewModel()
{


    private val mutableFavorites: MutableLiveData<List<Product>> = MutableLiveData()
    val favorites: LiveData<List<Product>> = mutableFavorites

    private val mutableMessage: MutableLiveData<String> = MutableLiveData("")
    val message: LiveData<String> = mutableMessage

     fun getFavoriteProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favoriteList = repository.getProducts()
                favoriteList.collect {
                    mutableFavorites.postValue(it)
                }
                Log.i("ViewModel", "Fetched from Database: $favoriteList")

            } catch (e: Exception) {
                mutableMessage.postValue("Error loading favorites: ${e.message}")
            }
        }
    }

    fun removeFromFavorites(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteProduct(product)
                getFavoriteProducts() // Refresh favorites list
                mutableMessage.postValue("Removed from favorites")
            } catch (e: Exception) {
                mutableMessage.postValue("Failed to remove product: ${e.message}")
            }
        }
    }

    class FavoritesViewModelFactory(private val repository: ProductsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(repository ) as T
        }

    }


}



