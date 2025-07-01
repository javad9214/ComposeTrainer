package com.example.composetrainer.domain.model

data class ProductSalesSummary(
    val timeRange: TimeRange,
    val products: List<TopSellingProductInfo>
)