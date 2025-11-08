package com.example.composetrainer.domain.usecase.product

import android.util.Log
import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.domain.repository.ProductRepository
import javax.inject.Inject

class AddProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend operator fun invoke(product: Product){
        Log.i(TAG, "invoke: ${product.id.value}")
        Log.i(TAG, "invoke: ${product.name.value}")
        productRepository.addProduct(product)
    }

    companion object{
        const val TAG = "AddProductUseCase"
    }

}