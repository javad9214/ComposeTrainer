package com.example.composetrainer.domain.usecase.sales

import com.example.composetrainer.domain.repository.ProductSalesSummaryRepository
import javax.inject.Inject

class AddToProductSalesSummaryUseCase @Inject constructor(
    private val productSalesSummaryRepository: ProductSalesSummaryRepository
) {
    suspend operator fun invoke(products: List<ProductWithQuantity>) {
        products.forEach { productWithQuantity ->
            productSalesSummaryRepository.addProductSale(
                productId = productWithQuantity.product.id.value,
                quantity = productWithQuantity.quantity
            )
        }
    }
}