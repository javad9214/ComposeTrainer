package com.example.composetrainer.domain.usecase.analytics

import com.example.composetrainer.domain.repository.InvoiceRepository
import com.example.composetrainer.utils.dateandtime.TimeRange
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getCurrentShamsiMonthStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getCurrentWeekStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getLastShamsiMonthStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getLastWeekStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getTodayStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getYesterdayStartEndMillis
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInvoiceReportCountUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {

    operator fun invoke(timeRange: TimeRange): Flow<Int> {
        val (start, end) = timeRange.getStartAndEndTimes()
        return invoiceRepository.getTotalInvoicesBetweenDates(start, end)
    }


    companion object {
        private const val TAG = "GetInvoiceReportCountUseCase"
    }
}