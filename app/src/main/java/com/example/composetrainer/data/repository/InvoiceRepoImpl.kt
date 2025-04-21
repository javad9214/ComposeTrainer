package com.example.composetrainer.data.repository

import com.example.composetrainer.data.local.dao.InvoiceDao
import com.example.composetrainer.data.local.dao.InvoiceProductDao
import com.example.composetrainer.data.local.dao.ProductDao
import com.example.composetrainer.data.local.entity.InvoiceEntity
import com.example.composetrainer.data.local.entity.InvoiceProductCrossRef
import com.example.composetrainer.data.mapper.ProductMapper
import com.example.composetrainer.domain.model.InvoiceWithProducts
import com.example.composetrainer.domain.model.ProductWithQuantity
import com.example.composetrainer.domain.repository.InvoiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.*

class InvoiceRepoImpl @Inject constructor(
    private val invoiceDao: InvoiceDao,
    private val invoiceProductDao: InvoiceProductDao,
    private val productDao: ProductDao
) : InvoiceRepository {

    override suspend fun createInvoice(products: List<ProductWithQuantity>) {
        val nextNumberId = getNextInvoiceNumberId()

        val invoiceEntity = InvoiceEntity(
            numberId = nextNumberId,
            dateTime = System.currentTimeMillis()
        )

        // Insert invoice and get its generated ID
        val invoiceId = invoiceDao.insertInvoice(invoiceEntity)

        // Insert Product into invoice
        products.forEach { productWithQuantity ->
            invoiceProductDao.insertCrossRef(
                InvoiceProductCrossRef(
                    invoiceId = invoiceId,
                    productId = productWithQuantity.product.id,
                    quantity = productWithQuantity.quantity
                )
            )

            // Update product stock
            productDao.updateProduct(
                ProductMapper.toEntity(
                    productWithQuantity.product.copy(
                        stock = productWithQuantity.product.stock - productWithQuantity.quantity
                    )
                )

            )

        }
    }

    override suspend fun getAllInvoices(): Flow<List<InvoiceWithProducts>> {
        return invoiceDao.getAllInvoiceWithProducts()
            .map { rows ->
                rows
                    .groupBy { Triple(it.invoiceId, it.numberId, it.dateTime) }
                    .map { (key, groupRows) ->
                        val (invoiceId, numberId, dateTime) = key
                        InvoiceWithProducts(
                            invoice = InvoiceEntity(invoiceId, numberId, dateTime),
                            products = groupRows.map {
                                ProductWithQuantity(ProductMapper.toDomain(it.product), it.quantity)
                            }
                        )
                    }
            }
    }



    override suspend fun getInvoiceWithProducts(invoiceId: Long): InvoiceWithProducts {
        val rows = invoiceDao.getInvoiceWithProducts(invoiceId)

        if (rows.isEmpty()) throw Exception("Invoice not found")

        val invoice = InvoiceEntity(
            id = rows.first().invoiceId,
            numberId = rows.first().numberId,
            dateTime = rows.first().dateTime
        )

        val products = rows.map {
            ProductWithQuantity(ProductMapper.toDomain(it.product), it.quantity)
        }

        return InvoiceWithProducts(invoice, products)
    }


    override suspend fun deleteInvoice(invoiceId: Long) {
        withContext(Dispatchers.IO) {
            val invoiceWithProducts = getInvoiceWithProducts(invoiceId)
            invoiceWithProducts.products.forEach { productWithQuantity ->
                productDao.updateProduct(
                    ProductMapper.toEntity(
                        productWithQuantity.product.copy(
                            stock = productWithQuantity.product.stock + productWithQuantity.quantity
                        )
                    )
                )
            }
            invoiceDao.deleteInvoice(invoiceId)
        }
    }

    private suspend fun getNextInvoiceNumberId(): Long {
        // Get the highest existing numberId and increment
        val lastInvoice = invoiceDao.getLastInvoice()
        return if (lastInvoice == null) 10000L else lastInvoice.numberId + 1
    }

}