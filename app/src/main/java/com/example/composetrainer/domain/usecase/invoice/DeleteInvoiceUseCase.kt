package com.example.composetrainer.domain.usecase.invoice

import com.example.composetrainer.domain.repository.InvoiceRepository
import javax.inject.Inject

class DeleteInvoiceUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {
    suspend operator fun invoke(invoiceId: Long) {
        invoiceRepository.deleteInvoice(invoiceId)
    }

    suspend fun deleteMultiple(invoiceIds: List<Long>) {
        invoiceIds.forEach { invoiceId ->
            invoiceRepository.deleteInvoice(invoiceId)
        }
    }
}