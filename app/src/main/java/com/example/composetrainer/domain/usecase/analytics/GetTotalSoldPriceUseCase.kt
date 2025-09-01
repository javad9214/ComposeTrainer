package com.example.composetrainer.domain.usecase.analytics

import android.util.Log
import com.example.composetrainer.domain.model.type.Money
import com.example.composetrainer.domain.repository.InvoiceRepository
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getCurrentShamsiMonthStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getCurrentWeekStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getLastShamsiMonthStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getLastWeekStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getTodayStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getYesterdayStartEndMillis
import javax.inject.Inject

class GetTotalSoldPriceUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {


    suspend fun getTodayTotalSold(): Money {
        val (start, end) = getTodayStartEndMillis()
        return Money(invoiceRepository.getTotalSalesBetweenDates(start, end))
    }

    suspend fun getYesterdayTotalSold(): Money {
        val (start, end) = getYesterdayStartEndMillis()
        return Money(invoiceRepository.getTotalSalesBetweenDates(start, end))
    }

    suspend fun getThisWeekTotalSold(): Money {
        val (start, end) = getCurrentWeekStartEndMillis()
        return Money(invoiceRepository.getTotalSalesBetweenDates(start, end))
    }

    suspend fun getLastWeekTotalSold(): Money {
        val (start, end) = getLastWeekStartEndMillis()
        return Money(invoiceRepository.getTotalSalesBetweenDates(start, end))
    }

    suspend fun getCurrentMonthTotalSold(): Money {
        val (start, end) = getCurrentShamsiMonthStartEndMillis()
        return Money(invoiceRepository.getTotalSalesBetweenDates(start, end))
    }

    suspend fun getLastMonthTotalSold(): Money {
        val (start, end) = getLastShamsiMonthStartEndMillis()
        return Money(invoiceRepository.getTotalSalesBetweenDates(start, end))
    }

    suspend fun getCustomRangeTotalSold(startMillis: Long, endMillis: Long): Money {
        return Money(invoiceRepository.getTotalSalesBetweenDates(startMillis, endMillis))
    }
}