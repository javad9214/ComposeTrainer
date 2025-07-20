package com.example.composetrainer.domain.usecase.analytics

import android.util.Log
import com.example.composetrainer.domain.repository.InvoiceRepository
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getCurrentShamsiMonthStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getCurrentShamsiWeekStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getLastShamsiMonthStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getLastShamsiWeekStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getTodayStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getYesterdayShamsiStartEndMillis
import javax.inject.Inject

class GetInvoiceReportCountUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {

    suspend fun getTodayInvoiceCount(): Int {
        val (start, end) = getTodayStartEndMillis()
        Log.i(TAG, "getTodayInvoiceCount:  start=$start, end=$end")
        return invoiceRepository.getTotalInvoicesBetweenDates(start, end)
    }

    suspend fun getYesterdayInvoiceCount(): Int {
        val (start, end) = getYesterdayShamsiStartEndMillis()
        Log.i(TAG, "getYesterdayInvoiceCount: start=$start, end=$end")
        return invoiceRepository.getTotalInvoicesBetweenDates(start, end)
    }

    suspend fun getThisWeekInvoiceCount(): Int {
        val (start, end) = getCurrentShamsiWeekStartEndMillis()
        Log.i(TAG, "getThisWeekInvoiceCount: start=$start, end=$end")
        return invoiceRepository.getTotalInvoicesBetweenDates(start, end)
    }

    suspend fun getLastWeekInvoiceCount(): Int {
        val (start, end) = getLastShamsiWeekStartEndMillis()
        Log.i(TAG, "getLastWeekInvoiceCount: start=$start, end=$end")
        return invoiceRepository.getTotalInvoicesBetweenDates(start, end)
    }

    suspend fun getCurrentMonthInvoiceCount(): Int {
        val (start, end) = getCurrentShamsiMonthStartEndMillis()
        Log.i(TAG, "getCurrentMonthInvoiceCount: start=$start, end=$end")
        return invoiceRepository.getTotalInvoicesBetweenDates(start, end)
    }

    suspend fun getLastMonthInvoiceCount(): Int {
        val (start, end) = getLastShamsiMonthStartEndMillis()
        Log.i(TAG, "getLastMonthInvoiceCount: start=$start, end=$end")
        return invoiceRepository.getTotalInvoicesBetweenDates(start, end)
    }

    suspend fun getCustomRangeInvoiceCount(startMillis: Long, endMillis: Long): Int {
        return invoiceRepository.getTotalInvoicesBetweenDates(startMillis, endMillis)
    }

    companion object {
        private const val TAG = "GetInvoiceReportCountUseCase"
    }
}