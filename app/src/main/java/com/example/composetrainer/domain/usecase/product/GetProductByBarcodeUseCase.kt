package com.example.composetrainer.domain.usecase.product

import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.domain.repository.ProductRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductByBarcodeUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(barcode: String): Product? {
        // Use the searchProducts method which already includes barcode search
        return productRepository.searchProducts(barcode)
            .map { products ->
            // Filter for exact barcode match and return the first match, or null if none found
                products.firstOrNull { it.barcode?.value == barcode }
            }
            .firstOrNull()
    }
}