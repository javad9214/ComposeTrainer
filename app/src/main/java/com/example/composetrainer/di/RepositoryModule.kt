package com.example.composetrainer.di

import com.example.composetrainer.data.repository.InvoiceRepoImpl
import com.example.composetrainer.data.repository.ProductRepoImpl
import com.example.composetrainer.data.repository.ProductSalesSummaryRepoImpl
import com.example.composetrainer.domain.repository.InvoiceRepository
import com.example.composetrainer.domain.repository.ProductRepository
import com.example.composetrainer.domain.repository.ProductSalesSummaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        repoImpl: ProductRepoImpl
    ): ProductRepository = repoImpl

    @Provides
    @Singleton
    fun provideInvoiceRepository(
        repoImpl: InvoiceRepoImpl
    ): InvoiceRepository = repoImpl

    @Provides
    @Singleton
    fun provideProductSalesSummaryRepository(
        repoImpl: ProductSalesSummaryRepoImpl
    ): ProductSalesSummaryRepository = repoImpl
}