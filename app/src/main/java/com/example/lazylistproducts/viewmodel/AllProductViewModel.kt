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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AllProductViewModel (private val repository: ProductsRepository) : ViewModel ()
{
    private val mutableProducts =MutableStateFlow<List<Product>>  (emptyList() )
    val products : StateFlow<List<Product>> = mutableProducts

    private val mutableMessage : MutableLiveData<String> = MutableLiveData("")

    init {
        fetchProductsFromApi()
    }

    fun fetchProductsFromApi() {
        viewModelScope.launch (Dispatchers.IO){

                val productList = repository.fetchProductsFromApi()
                    productList
                    .catch { e -> mutableMessage.postValue("Error fetching products: ${e.message}") }
                   .collect{
                        mutableProducts.value = it
                        Log.i("ViewModel", "Fetched from API: $productList")
                    }


        }
    }

    fun addToFavorites(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertProduct (product)
                mutableMessage.postValue("Added to favorites")
            } catch (e: Exception) {
                mutableMessage.postValue("Failed to add to favorites: ${e.message}")
            }
        }
    }

    class ProductViewModelFactory(private val repository: ProductsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AllProductViewModel(repository ) as T
        }

    }



}