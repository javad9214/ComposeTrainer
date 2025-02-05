package com.example.composetrainer.domain.usecase

import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.domain.repository.ProductRepository
import javax.inject.Inject

class AddProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend operator fun invoke(product: Product){
        productRepository.addProduct(product)
    }
}