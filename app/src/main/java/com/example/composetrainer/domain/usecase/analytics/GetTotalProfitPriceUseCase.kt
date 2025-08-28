package com.example.composetrainer.domain.usecase.analytics

import com.example.composetrainer.domain.repository.InvoiceRepository
import javax.inject.Inject
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getCurrentShamsiMonthStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getCurrentWeekStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getLastShamsiMonthStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getLastWeekStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getTodayStartEndMillis
import com.example.composetrainer.utils.dateandtime.TimeStampUtil.getYesterdayStartEndMillis
import com.example.composetrainer.domain.model.type.Money

class GetTotalProfitPriceUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {

    suspend fun getTodayTotalProfit(): Money {
        val (start, end) = getTodayStartEndMillis()
        return Money(invoiceRepository.getTotalProfitBetweenDates(start, end))
    }

    suspend fun getYesterdayTotalProfit(): Money {
        val (start, end) = getYesterdayStartEndMillis()
        return Money(invoiceRepository.getTotalProfitBetweenDates(start, end))
    }

    suspend fun getThisWeekTotalProfit(): Money {
        val (start, end) = getCurrentWeekStartEndMillis()
        return Money(invoiceRepository.getTotalProfitBetweenDates(start, end))
    }

    suspend fun getLastWeekTotalProfit(): Money {
        val (start, end) = getLastWeekStartEndMillis()
        return Money(invoiceRepository.getTotalProfitBetweenDates(start, end))
    }

    suspend fun getCurrentMonthTotalProfit(): Money {
        val (start, end) = getCurrentShamsiMonthStartEndMillis()
        return Money(invoiceRepository.getTotalProfitBetweenDates(start, end))
    }

    suspend fun getLastMonthTotalProfit(): Money {
        val (start, end) = getLastShamsiMonthStartEndMillis()
        return Money(invoiceRepository.getTotalProfitBetweenDates(start, end))
    }

    suspend fun getCustomRangeTotalProfit(startMillis: Long, endMillis: Long): Money {
        return Money(invoiceRepository.getTotalProfitBetweenDates(startMillis, endMillis))
    }
}