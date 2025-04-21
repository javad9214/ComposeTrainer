package com.example.composetrainer.domain.model

import com.example.composetrainer.data.local.entity.InvoiceEntity

data class InvoiceWithProducts (
    val invoice: InvoiceEntity,
    val products: List<ProductWithQuantity>
){
    fun toDomain(): Invoice {
        return Invoice(
            id = invoice.id,
            numberId = invoice.numberId,
            dateTime = invoice.dateTime,
            products = products,
            totalPrice = products.sumOf { it.product.price?.times(it.quantity) ?: 0L},
        )
    }
}

