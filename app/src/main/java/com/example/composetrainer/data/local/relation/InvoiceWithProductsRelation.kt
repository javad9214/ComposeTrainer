package com.example.composetrainer.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.composetrainer.data.local.entity.InvoiceEntity
import com.example.composetrainer.data.local.entity.InvoiceProductCrossRefEntity

data class InvoiceWithProductsRelation (
    @Embedded val invoice: InvoiceEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "invoiceId",
        entity = InvoiceProductCrossRefEntity::class
    )
    val invoiceProducts: List<InvoiceProductCrossRefEntity>

)