package com.example.composetrainer.domain.usecase.sales

import com.example.composetrainer.domain.model.ProductSalesSummary
import com.example.composetrainer.domain.model.TimeRange
import com.example.composetrainer.domain.model.toDomain
import com.example.composetrainer.domain.repository.ProductRepository
import com.example.composetrainer.domain.repository.ProductSalesSummaryRepository
import javax.inject.Inject

class GetProductSalesSummaryUseCase @Inject constructor(
    private val productSalesSummaryRepository: ProductSalesSummaryRepository,
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(timeRange: TimeRange): List<ProductSalesSummary> {
        val (startTime, endTime) = timeRange.getStartAndEndTimes()
        val summaries =
            productSalesSummaryRepository.getTopSellingProductsBetween(startTime, endTime)

        return summaries.map { it.toDomain() }
    }


}