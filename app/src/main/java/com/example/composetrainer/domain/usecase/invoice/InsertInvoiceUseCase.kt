package com.example.composetrainer.domain.usecase.invoice

import android.util.Log
import com.example.composetrainer.domain.model.InvoiceId
import com.example.composetrainer.domain.model.InvoiceType
import com.example.composetrainer.domain.model.InvoiceWithProducts
import com.example.composetrainer.domain.model.StockMovementFactory
import com.example.composetrainer.domain.model.updateInvoiceId
import com.example.composetrainer.domain.repository.InvoiceProductRepository
import com.example.composetrainer.domain.repository.InvoiceRepository
import com.example.composetrainer.domain.repository.StockMovementRepository
import com.example.composetrainer.domain.usecase.sales.SaveProductSaleSummeryUseCase
import javax.inject.Inject

const val TAG = "InsertInvoiceUseCase"

class InsertInvoiceUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val stockMovementRepository: StockMovementRepository,
    private val invoiceProductRepository: InvoiceProductRepository,
    private val saveProductSaleSummeryUseCase: SaveProductSaleSummeryUseCase
) {
    suspend operator fun invoke(invoiceWithProducts: InvoiceWithProducts) {

        // update invoice id to zero so the Room will create it automatically
        invoiceWithProducts.invoice.updateInvoiceId(InvoiceId(0))

        // save Invoice
        val invoiceId = invoiceRepository.createInvoice(invoiceWithProducts.invoice)

        // update invoiceId to all relatives
        invoiceWithProducts.updateInvoiceId(InvoiceId(invoiceId))

        // save InvoiceProduct
        invoiceWithProducts.invoiceProducts.forEachIndexed { index, invoiceProduct ->
            try {
                invoiceProductRepository.insertCrossRef(invoiceProduct)

            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "invoke: Error processing InvoiceProduct for item $index - ProductId: ${invoiceProduct.productId}",
                    e
                )
                throw e
            }
        }

        if (invoiceWithProducts.invoice.invoiceType == InvoiceType.SALE) {
            insertSaleInvoice(invoiceWithProducts)
        } else insertPurchaseInvoice(invoiceWithProducts)

    }

    private suspend fun insertSaleInvoice(invoiceWithProducts: InvoiceWithProducts) {
        // save ProductSalesSummary
        invoiceWithProducts.invoiceProducts.forEachIndexed { index, invoiceProduct ->
            try {

                saveProductSaleSummeryUseCase.invoke(invoiceProduct)

            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "invoke: Error processing ProductSalesSummary for item $index - ProductId: ${invoiceProduct.productId}",
                    e
                )
                // Re-throw to maintain original behavior, but with better logging
                throw e
            }
        }

        // save StockMovement
        invoiceWithProducts.invoiceProducts.forEachIndexed { index, invoiceProduct ->
            try {
                stockMovementRepository.insert(
                    StockMovementFactory.createSale(
                        productId = invoiceProduct.productId.value,
                        quantitySold = invoiceProduct.quantity.value,
                        invoiceId = invoiceProduct.invoiceId.value,
                    )
                )
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "invoke: Error processing StockMovement for item $index - ProductId: ${invoiceProduct.productId}",
                    e
                )
                throw e
            }
        }
    }

    private suspend fun insertPurchaseInvoice(invoiceWithProducts: InvoiceWithProducts) {
        // save StockMovement
        invoiceWithProducts.invoiceProducts.forEachIndexed { index, invoiceProduct ->
            try {
                stockMovementRepository.insert(
                    StockMovementFactory.createPurchase(
                        productId = invoiceProduct.productId.value,
                        quantityPurchased = invoiceProduct.quantity.value,
                        invoiceId = invoiceProduct.invoiceId.value,
                    )
                )
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "invoke: Error processing StockMovement for item $index - ProductId: ${invoiceProduct.productId}",
                    e
                )
                throw e
            }
        }
    }
}