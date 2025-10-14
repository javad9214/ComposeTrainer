package com.example.composetrainer.domain.usecase.analytics

import com.example.composetrainer.domain.repository.InvoiceRepository
import com.example.composetrainer.utils.dateandtime.TimeRange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalProfitPriceUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {

    operator fun invoke(timeRange: TimeRange): Flow<Long> {
        val (start, end) = timeRange.getStartAndEndTimes()
        return invoiceRepository.getTotalProfitBetweenDates(start, end)
    }


}