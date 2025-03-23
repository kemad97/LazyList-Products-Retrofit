package com.example.lazylistproducts.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lazylistproducts.Response
import com.example.lazylistproducts.model.Product
import com.example.lazylistproducts.repo.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AllProductViewModel (private val repository: ProductsRepository) : ViewModel ()
{
    private val _products = MutableStateFlow<Response>(Response.Loading)
    val products: StateFlow<Response> = _products

    private val mutableMessage : MutableStateFlow<String> = MutableStateFlow("")

    init {
        fetchProductsFromApi()
    }

    fun fetchProductsFromApi() {
        viewModelScope.launch (Dispatchers.IO){

                val productList = repository.fetchProductsFromApi()

                    productList
                    .catch { e -> mutableMessage.value= e.message.toString() }
                        .collect { prod ->
                            _products.value = Response.Success(prod)
                            Log.i("ViewModel", "Fetched from API: $productList")
                        }

        }
    }

    fun addToFavorites(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertProduct (product)
                mutableMessage.value=("Added to favorites")
            } catch (e: Exception) {
                mutableMessage.value=("Failed to add to favorites: ${e.message}")
            }
        }
    }

    class ProductViewModelFactory(private val repository: ProductsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AllProductViewModel(repository ) as T
        }

    }



}