package com.example.composetrainer.domain.repository

import com.example.composetrainer.data.local.entity.ProductSalesSummaryEntity
import com.example.composetrainer.domain.model.ProductSalesSummary

interface ProductSalesSummaryRepository {
    suspend fun addProductSale(productSalesSummary: ProductSalesSummary)
    suspend fun getTopSellingProductsBetween(
        start: Long,
        end: Long
    ): List<ProductSalesSummary>

    suspend fun getByProductAndDate(productId: Long, date: Long): ProductSalesSummaryEntity?
}