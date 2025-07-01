package com.example.composetrainer.domain.usecase.sales

import com.example.composetrainer.data.local.entity.ProductSalesSummaryEntity
import com.example.composetrainer.domain.model.ProductSalesSummary
import com.example.composetrainer.domain.model.TimeRange
import com.example.composetrainer.domain.model.TopSellingProductInfo
import com.example.composetrainer.domain.repository.ProductRepository
import com.example.composetrainer.domain.repository.ProductSalesSummaryRepository
import javax.inject.Inject

class GetProductSalesSummaryUseCase @Inject constructor(
    private val productSalesSummaryRepository: ProductSalesSummaryRepository,
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(timeRange: TimeRange): ProductSalesSummary {
        val (startTime, endTime) = timeRange.getStartAndEndTimes()
        val summaries =
            productSalesSummaryRepository.getTopSellingProductsBetween(startTime, endTime)

        return ProductSalesSummary(
            timeRange = timeRange,
            products = mapToTopSellingProductInfo(summaries)
        )
    }

    private suspend fun mapToTopSellingProductInfo(
        summaries: List<ProductSalesSummaryEntity>
    ): List<TopSellingProductInfo> {
        val result = mutableListOf<TopSellingProductInfo>()

        for (summary in summaries) {
            val product = productRepository.getProductById(summary.productId)
            if (product != null) {
                result.add(
                    TopSellingProductInfo(
                        name = product.name,
                        totalQuantity = summary.totalSold,
                        totalSales = product.price?.times(summary.totalSold) ?: 0L
                    )
                )
            }
        }

        return result
    }
}