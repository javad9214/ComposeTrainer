package com.example.composetrainer.domain.repository

import com.example.composetrainer.domain.model.InvoiceProduct


interface InvoiceProductRepository {

    suspend fun insertCrossRef(invoiceProduct: InvoiceProduct)


    suspend fun deleteCrossRef(invoiceProduct: InvoiceProduct)


    suspend fun getInvoiceWithProducts(invoiceId: Long): List<InvoiceProduct>


    suspend fun getAllInvoiceWithProducts(invoiceId: Long): List<InvoiceProduct>
}
