package com.example.composetrainer.domain.usecase.invoice

import com.example.composetrainer.domain.repository.InvoiceRepository
import javax.inject.Inject

class GetInvoiceNumberUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(): Long {
        return repository.getNextInvoiceNumberId()
    }
}
