package com.example.composetrainer.domain.repository

import com.example.composetrainer.data.local.entity.ProductSalesSummaryEntity
import com.example.composetrainer.domain.model.ProductSalesSummary
import kotlinx.coroutines.flow.Flow

interface ProductSalesSummaryRepository {
    suspend fun insertProductSale(productSalesSummary: ProductSalesSummary)
    suspend fun updateProductSale(productSalesSummary: ProductSalesSummary)

    fun getTopSellingProductsBetween(
        start: Long,
        end: Long
    ): Flow<List<ProductSalesSummary>>

    suspend fun getByProductAndDate(productId: Long, date: Long): ProductSalesSummaryEntity?
}