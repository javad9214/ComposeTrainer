package com.example.composetrainer.domain.model

import com.example.composetrainer.data.local.entity.InvoiceEntity

package com.example.composetrainer.domain.model

import com.example.composetrainer.data.local.entity.InvoiceProductCrossRefEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

// Domain Model
data class InvoiceProduct(
    val invoiceId: InvoiceId,
    val productId: ProductId,
    val quantity: Quantity,
    val priceAtSale: Money,
    val costPriceAtTransaction: Money,
    val discount: Money,
    val total: Money,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    // Business logic methods
    fun calculateProfit(): Money {
        return Money((priceAtSale.amount - costPriceAtTransaction.amount) * quantity.value)
    }

    fun calculateProfitAfterDiscount(): Money {
        return Money((priceAtSale.amount - discount.amount - costPriceAtTransaction.amount) * quantity.value)
    }

    fun isDiscounted(): Boolean {
        return discount.amount > 0
    }

    fun discountPercentage(): Double {
        return if (priceAtSale.amount > 0) {
            (discount.amount.toDouble() / priceAtSale.amount.toDouble()) * 100
        } else {
            0.0
        }
    }

    fun hasProfit(): Boolean {
        return priceAtSale.amount > costPriceAtTransaction.amount
    }

    fun isAtBreakEven(): Boolean {
        return priceAtSale.amount == costPriceAtTransaction.amount
    }

    fun isSoldAtLoss(): Boolean {
        return priceAtSale.amount < costPriceAtTransaction.amount
    }
}

// Value Objects
@JvmInline
value class InvoiceId(val value: Long) {
    init {
        require(value > 0) { "Invoice ID must be positive" }
    }
}

@JvmInline
value class ProductId(val value: Long) {
    init {
        require(value > 0) { "Product ID must be positive" }
    }
}

@JvmInline
value class Quantity(val value: Int) {
    init {
        require(value > 0) { "Quantity must be positive" }
    }
}

// Reusing your existing Money value class
// @JvmInline
// value class Money(val amount: Long)

// Mapping Extension Functions
fun InvoiceProductCrossRefEntity.toDomain(): InvoiceProduct {
    return InvoiceProduct(
        invoiceId = InvoiceId(invoiceId),
        productId = ProductId(productId),
        quantity = Quantity(quantity),
        priceAtSale = Money(priceAtSale),
        costPriceAtTransaction = Money(costPriceAtTransaction),
        discount = Money(discount),
        total = Money(total),
        createdAt = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(createdAt), // Assuming you have createdAt in entity
            ZoneId.systemDefault()
        ),
        updatedAt = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(updatedAt), // Assuming you have updatedAt in entity
            ZoneId.systemDefault()
        )
    )
}

fun InvoiceProduct.toEntity(): InvoiceProductCrossRefEntity {
    return InvoiceProductCrossRefEntity(
        invoiceId = invoiceId.value,
        productId = productId.value,
        quantity = quantity.value,
        priceAtSale = priceAtSale.amount,
        costPriceAtTransaction = costPriceAtTransaction.amount,
        discount = discount.amount,
        total = total.amount
        // createdAt and updatedAt would be set here if they exist in entity
    )
}

// Factory for creating new invoice products
object InvoiceProductFactory {
    fun create(
        invoiceId: Long,
        productId: Long,
        quantity: Int,
        priceAtSale: Long,
        costPriceAtTransaction: Long,
        discount: Long = 0
    ): InvoiceProduct {
        val now = LocalDateTime.now()
        return InvoiceProduct(
            invoiceId = InvoiceId(invoiceId),
            productId = ProductId(productId),
            quantity = Quantity(quantity),
            priceAtSale = Money(priceAtSale),
            costPriceAtTransaction = Money(costPriceAtTransaction),
            discount = Money(discount),
            total = Money((priceAtSale - discount) * quantity),
            createdAt = now,
            updatedAt = now
        )
    }
}

// Extension functions for updating invoice product
fun InvoiceProduct.updateQuantity(newQuantity: Int): InvoiceProduct {
    return copy(
        quantity = Quantity(newQuantity),
        total = Money((priceAtSale.amount - discount.amount) * newQuantity),
        updatedAt = LocalDateTime.now()
    )
}

fun InvoiceProduct.updatePrice(newPrice: Long): InvoiceProduct {
    return copy(
        priceAtSale = Money(newPrice),
        total = Money((newPrice - discount.amount) * quantity.value),
        updatedAt = LocalDateTime.now()
    )
}

fun InvoiceProduct.applyDiscount(newDiscount: Long): InvoiceProduct {
    return copy(
        discount = Money(newDiscount),
        total = Money((priceAtSale.amount - newDiscount) * quantity.value),
        updatedAt = LocalDateTime.now()
    )
}

fun InvoiceProduct.adjustCostPrice(newCostPrice: Long): InvoiceProduct {
    return copy(
        costPriceAtTransaction = Money(newCostPrice),
        updatedAt = LocalDateTime.now()
    )
}