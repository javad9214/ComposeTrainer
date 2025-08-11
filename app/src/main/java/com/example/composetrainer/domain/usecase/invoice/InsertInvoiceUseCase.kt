package com.example.composetrainer.domain.usecase.invoice

import com.example.composetrainer.domain.model.InvoiceWithProducts
import com.example.composetrainer.domain.model.StockMovementFactory
import com.example.composetrainer.domain.repository.InvoiceProductRepository
import com.example.composetrainer.domain.repository.InvoiceRepository
import com.example.composetrainer.domain.repository.ProductSalesSummaryRepository
import com.example.composetrainer.domain.repository.StockMovementRepository
import javax.inject.Inject

class InsertInvoiceUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val productSalesSummaryRepository: ProductSalesSummaryRepository,
    private val stockMovementRepository: StockMovementRepository,
    private val invoiceProductRepository: InvoiceProductRepository
) {
    suspend operator fun invoke(invoiceWithProducts: InvoiceWithProducts) {

        // save Invoice
        invoiceRepository.createInvoice(invoiceWithProducts.invoice )

        // save InvoiceProduct
        invoiceWithProducts.invoiceProducts.forEach { invoiceProduct ->
            invoiceProductRepository.insertCrossRef(invoiceProduct)
        }

        // save ProductSalesSummary
        invoiceWithProducts.invoiceProducts.forEach { invoiceProduct ->
            productSalesSummaryRepository.addProductSale(
                productId = invoiceProduct.productId.value,
                quantity = invoiceProduct.quantity.value
            )
        }

        // save StockMovement
        invoiceWithProducts.invoiceProducts.forEach { invoiceProduct ->
            stockMovementRepository.insert(
                StockMovementFactory.createSale(
                    productId = invoiceProduct.productId.value,
                    quantitySold = invoiceProduct.quantity.value,
                    invoiceId = invoiceProduct.invoiceId.value,
                )
            )
        }
    }
}