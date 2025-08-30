package com.example.composetrainer.data.repository

import android.util.Log
import com.example.composetrainer.data.local.dao.InvoiceProductDao
import com.example.composetrainer.domain.model.InvoiceProduct
import com.example.composetrainer.domain.model.toDomain
import com.example.composetrainer.domain.model.toEntity
import com.example.composetrainer.domain.repository.InvoiceProductRepository
import javax.inject.Inject

const val TAG = "InvoiceProductRepoImpl"

class InvoiceProductRepoImpl @Inject constructor(
    private val invoiceProductDao: InvoiceProductDao
): InvoiceProductRepository {

    override suspend fun insertCrossRef(invoiceProduct: InvoiceProduct) {
        try {
            invoiceProductDao.insertCrossRef(invoiceProduct.toEntity())
        }catch (e: Exception){
            Log.i(TAG, "insertCrossRef: failed ${e.message}")
        }

    }

    override suspend fun deleteCrossRef(invoiceProduct: InvoiceProduct) {
        invoiceProductDao.deleteCrossRef(invoiceProduct.toEntity())
    }

    override suspend fun getInvoiceWithProducts(invoiceId: Long): List<InvoiceProduct> {
       return invoiceProductDao.getInvoiceWithProducts(invoiceId).map { it.toDomain() }
    }

    override suspend fun getAllInvoiceWithProducts(invoiceId: Long): List<InvoiceProduct> {
        return invoiceProductDao.getAllInvoiceWithProducts(invoiceId).map { it.toDomain() }
    }
}