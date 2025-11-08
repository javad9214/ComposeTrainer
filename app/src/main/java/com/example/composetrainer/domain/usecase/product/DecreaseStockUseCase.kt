package com.example.composetrainer.domain.usecase.product


import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.domain.model.Quantity
import com.example.composetrainer.domain.model.reduceStock
import com.example.composetrainer.domain.repository.ProductRepository
import javax.inject.Inject

class DecreaseStockUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product , quantity: Int = 1){
        if (product.isInStock()) {
            repository.updateProduct(product.reduceStock(quantity))
        }
    }
}