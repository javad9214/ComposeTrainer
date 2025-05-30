package com.example.composetrainer.domain.usecase.analytics

import android.util.Log
import com.example.composetrainer.data.repository.InvoiceRepoImpl
import com.example.composetrainer.domain.model.AnalyticsData
import com.example.composetrainer.domain.model.MonthlySummary
import com.example.composetrainer.domain.repository.InvoiceRepository
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

class GetAnalyticsDataUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {
    suspend operator fun invoke(): AnalyticsData {
        val currentYearMonth = getCurrentYearMonth()
        Log.d("GetAnalyticsDataUseCase", "Querying for year-month: $currentYearMonth")

        // Debug: Check if there are any invoices at all
        try {
            val totalInvoiceCount =
                (invoiceRepository as InvoiceRepoImpl).invoiceDao.getTotalInvoiceCount()
            Log.d("GetAnalyticsDataUseCase", "Total invoices in database: $totalInvoiceCount")

            val recentInvoices =
                invoiceRepository.invoiceDao.getRecentInvoicesForDebug()
            Log.d(
                "GetAnalyticsDataUseCase",
                "Recent invoice dates: ${recentInvoices.map { it.invoiceDate }}"
            )
        } catch (e: Exception) {
            Log.e("GetAnalyticsDataUseCase", "Debug query failed", e)
        }

        val totalSales = invoiceRepository.getTotalSalesForMonth(currentYearMonth)
        val invoiceCount = invoiceRepository.getTotalInvoicesForMonth(currentYearMonth)
        val totalQuantity = invoiceRepository.getTotalQuantityForMonth(currentYearMonth)
        val topSellingProducts = invoiceRepository.getTopSellingProductsForMonth(currentYearMonth)

        Log.d(
            "GetAnalyticsDataUseCase",
            "Results - Sales: $totalSales, Invoices: $invoiceCount, Quantity: $totalQuantity, Top products: ${topSellingProducts.size}"
        )

        val monthlySummary = MonthlySummary(
            totalSales = totalSales,
            invoiceCount = invoiceCount,
            totalQuantity = totalQuantity
        )

        return AnalyticsData(
            monthlySummary = monthlySummary,
            topSellingProducts = topSellingProducts
        )
    }

    private fun getCurrentYearMonth(): String {
        val persianDate = PersianDate()
        val year = persianDate.shYear
        val month = persianDate.shMonth.toString().padStart(2, '0')
        return "$year/$month"  // Changed from dash to slash to match database format
    }
}
