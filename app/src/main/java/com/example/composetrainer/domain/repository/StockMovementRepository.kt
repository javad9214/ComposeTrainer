package com.example.composetrainer.domain.repository

import com.example.composetrainer.domain.model.StockMovement
import kotlinx.coroutines.flow.Flow


interface StockMovementRepository {

    suspend fun insert(movement: StockMovement): Long

    suspend fun update(movement: StockMovement)

    suspend fun delete(movement: StockMovement)


    fun getByProductId(productId: Long): Flow<List<StockMovement>>

    suspend fun getByInvoiceId(invoiceId: Long): List<StockMovement>
}
