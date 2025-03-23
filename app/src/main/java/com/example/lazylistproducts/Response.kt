package com.example.lazylistproducts

import com.example.lazylistproducts.model.Product

sealed class Response {
    data object Loading : Response()
    data class Success(val data: List<Product>) : Response()
    data class Failure(val message: String) : Response()
}