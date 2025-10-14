package com.example.composetrainer.domain.usecase.analytics

import com.example.composetrainer.domain.repository.InvoiceRepository
import javax.inject.Inject

class GetSoldProductsReportUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {

}