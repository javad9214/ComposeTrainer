package com.example.composetrainer.domain.usecase.invoice

import com.example.composetrainer.domain.repository.InvoiceRepository
import javax.inject.Inject

class InsertInvoiceUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {
    suspend operator fun invoke(products: List<ProductWithQuantity>){

    }
}