package com.example.composetrainer.domain.usecase


import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.domain.repository.ProductRepository
import javax.inject.Inject

class EditProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        repository.editProduct(product)
    }
}