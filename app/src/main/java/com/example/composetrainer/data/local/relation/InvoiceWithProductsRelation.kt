package com.example.composetrainer.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.composetrainer.data.local.entity.InvoiceEntity
import com.example.composetrainer.data.local.entity.InvoiceProductCrossRefEntity
import com.example.composetrainer.data.local.entity.ProductEntity

data class InvoiceWithProductsRelation (
    @Embedded val invoice: InvoiceEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "invoiceId",
        entity = InvoiceProductCrossRefEntity::class
    )
    val invoiceProducts: List<ProductsRelation>,

)

data class ProductsRelation(
    @Embedded val invoiceProductsCrossRef: InvoiceProductCrossRefEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: ProductEntity
)