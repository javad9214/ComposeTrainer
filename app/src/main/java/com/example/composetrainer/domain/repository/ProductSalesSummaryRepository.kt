package com.example.composetrainer.domain.repository

import com.example.composetrainer.data.local.entity.ProductSalesSummaryEntity

interface ProductSalesSummaryRepository {
    suspend fun addProductSale(productId: Long, quantity: Int)
    suspend fun getTopSellingProductsBetween(
        start: Long,
        end: Long
    ): List<ProductSalesSummaryEntity>

    suspend fun getByProductAndDate(productId: Long, date: Long): ProductSalesSummaryEntity?
}