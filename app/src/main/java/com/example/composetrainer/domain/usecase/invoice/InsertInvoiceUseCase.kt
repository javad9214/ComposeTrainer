package com.example.composetrainer.domain.usecase.invoice

import android.util.Log
import com.example.composetrainer.domain.model.InvoiceId
import com.example.composetrainer.domain.model.InvoiceWithProducts
import com.example.composetrainer.domain.model.StockMovementFactory
import com.example.composetrainer.domain.model.updateInvoiceId
import com.example.composetrainer.domain.repository.InvoiceProductRepository
import com.example.composetrainer.domain.repository.InvoiceRepository
import com.example.composetrainer.domain.repository.ProductSalesSummaryRepository
import com.example.composetrainer.domain.repository.StockMovementRepository
import javax.inject.Inject

const val TAG = "InsertInvoiceUseCase"

class InsertInvoiceUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val productSalesSummaryRepository: ProductSalesSummaryRepository,
    private val stockMovementRepository: StockMovementRepository,
    private val invoiceProductRepository: InvoiceProductRepository
) {
    suspend operator fun invoke(invoiceWithProducts: InvoiceWithProducts) {

        // update invoice id to zero so the Room will create it automatically
        invoiceWithProducts.invoice.updateInvoiceId(InvoiceId(0))

        // save Invoice
        val invoiceId = invoiceRepository.createInvoice(invoiceWithProducts.invoice)

        // update invoiceId to all relatives
        invoiceWithProducts.updateInvoiceId(InvoiceId(invoiceId))

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