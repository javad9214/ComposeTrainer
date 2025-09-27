package com.example.composetrainer.domain.usecase.invoice.purchase

import com.example.composetrainer.domain.model.InvoiceWithProducts
import com.example.composetrainer.domain.repository.InvoiceProductRepository
import com.example.composetrainer.domain.repository.InvoiceRepository
import com.example.composetrainer.domain.repository.StockMovementRepository
import com.example.composetrainer.domain.usecase.sales.SaveProductSaleSummeryUseCase
import javax.inject.Inject

class InsertPurchaseInvoiceUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val stockMovementRepository: StockMovementRepository,
    private val invoiceProductRepository: InvoiceProductRepository,
    private val saveProductSaleSummeryUseCase: SaveProductSaleSummeryUseCase
) {
    suspend operator fun invoke(invoiceWithProducts: InvoiceWithProducts) {

    }
}