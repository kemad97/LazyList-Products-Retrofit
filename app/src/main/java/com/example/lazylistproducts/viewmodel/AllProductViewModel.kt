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

class AllProductViewModel (private val repository: ProductsRepository) : ViewModel ()
{
    private val mutableProducts : MutableLiveData <List <Product>> = MutableLiveData()
    val products : LiveData <List <Product>> = mutableProducts

    private val mutableMessage : MutableLiveData<String> = MutableLiveData("")
    val message : LiveData<String>  = mutableMessage

    fun fetchProductsFromApi() {
        viewModelScope.launch (Dispatchers.IO){
            try {
                val productList = repository.fetchProductsFromApi()
                mutableProducts.postValue(productList)
                Log.i("ViewModel", "Fetched from API: ${productList.size}")

            }
            catch (e:Exception)
            {
                mutableMessage.postValue("Failed to load products: ${e.message}")
            }

        }
    }



    // Add product to favorites (local storage)
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