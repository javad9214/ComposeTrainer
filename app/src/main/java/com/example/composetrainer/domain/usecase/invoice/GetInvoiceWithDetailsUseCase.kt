package com.example.composetrainer.domain.usecase.invoice

import com.example.composetrainer.domain.model.InvoiceWithProducts
import com.example.composetrainer.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInvoiceWithDetailsUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {
    operator fun invoke(invoiceId: Long): Flow<InvoiceWithProducts> {
        return invoiceRepository.getInvoiceWithProducts(invoiceId)
    }
}