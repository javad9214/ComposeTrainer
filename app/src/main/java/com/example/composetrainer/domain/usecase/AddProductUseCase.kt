package com.example.composetrainer.domain.usecase

import com.example.composetrainer.data.local.entity.ProductEntity
import com.example.composetrainer.domain.repository.ProductRepository
import javax.inject.Inject

class AddProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend operator fun invoke(productEntity: ProductEntity){
        productRepository.addProduct(productEntity)
    }
}