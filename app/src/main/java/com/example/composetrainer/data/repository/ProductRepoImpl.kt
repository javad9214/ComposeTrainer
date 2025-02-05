package com.example.composetrainer.data.repository

import com.example.composetrainer.data.local.dao.ProductDao
import com.example.composetrainer.data.local.entity.ProductEntity
import com.example.composetrainer.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepoImpl @Inject constructor(
    private val productDao: ProductDao
): ProductRepository {

    override suspend fun addProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    override fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

}