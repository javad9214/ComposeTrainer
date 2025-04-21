package com.example.composetrainer.data.local.relation

import androidx.room.Embedded
import com.example.composetrainer.data.local.entity.ProductEntity

data class InvoiceWithProduct (
    val invoiceId: Long,
    val numberId: Long,
    val dateTime: Long,
    @Embedded val product: ProductEntity,
    val quantity: Int
)

