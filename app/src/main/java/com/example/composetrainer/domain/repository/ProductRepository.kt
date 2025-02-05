package com.example.composetrainer.domain.repository

import com.example.composetrainer.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun addProduct(product: ProductEntity)

    fun getAllProducts(): Flow<List<ProductEntity>>

}