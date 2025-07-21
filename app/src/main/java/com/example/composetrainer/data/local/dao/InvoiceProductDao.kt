package com.example.composetrainer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.example.composetrainer.data.local.entity.InvoiceProductCrossRefEntity

@Dao
interface InvoiceProductDao {
    @Insert
    suspend fun insertCrossRef(crossRef: InvoiceProductCrossRefEntity)

    @Delete
    suspend fun deleteCrossRef(crossRef: InvoiceProductCrossRefEntity)
}