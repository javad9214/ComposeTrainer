package com.example.composetrainer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.composetrainer.data.local.dao.InvoiceDao
import com.example.composetrainer.data.local.dao.InvoiceProductDao
import com.example.composetrainer.data.local.dao.ProductDao
import com.example.composetrainer.data.local.entity.InvoiceEntity
import com.example.composetrainer.data.local.entity.InvoiceProductCrossRef
import com.example.composetrainer.data.local.entity.ProductEntity


@Database(
    entities = [ProductEntity::class, InvoiceEntity::class, InvoiceProductCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun invoiceProductDao(): InvoiceProductDao
}