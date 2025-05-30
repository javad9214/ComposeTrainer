package com.example.composetrainer.domain.usecase.product


import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.domain.repository.ProductRepository
import javax.inject.Inject

class DecreaseStockUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product){
        if (product.stock > 0) {
            repository.updateProduct(product.copy(stock = product.stock - 1))
        }
    }
}