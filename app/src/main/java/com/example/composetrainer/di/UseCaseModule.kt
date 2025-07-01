package com.example.composetrainer.di

import com.example.composetrainer.domain.repository.InvoiceRepository
import com.example.composetrainer.domain.repository.ProductRepository
import com.example.composetrainer.domain.repository.ProductSalesSummaryRepository
import com.example.composetrainer.domain.usecase.product.AddProductUseCase
import com.example.composetrainer.domain.usecase.analytics.GetAnalyticsDataUseCase
import com.example.composetrainer.domain.usecase.product.GetProductUseCase
import com.example.composetrainer.domain.usecase.sales.AddToProductSalesSummaryUseCase
import com.example.composetrainer.domain.usecase.sales.GetProductSalesSummaryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideAddProductUseCase(
        repository: ProductRepository
    ): AddProductUseCase = AddProductUseCase(repository)

    @Provides
    @Singleton
    fun provideGetProductsUseCase(
        repository: ProductRepository
    ): GetProductUseCase = GetProductUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAnalyticsDataUseCase(
        invoiceRepository: InvoiceRepository
    ): GetAnalyticsDataUseCase = GetAnalyticsDataUseCase(invoiceRepository)

    @Provides
    @Singleton
    fun provideAddToProductSalesSummaryUseCase(
        repository: ProductSalesSummaryRepository
    ): AddToProductSalesSummaryUseCase = AddToProductSalesSummaryUseCase(repository)

    @Provides
    @Singleton
    fun provideGetProductSalesSummaryUseCase(
        productSalesSummaryRepository: ProductSalesSummaryRepository,
        productRepository: ProductRepository
    ): GetProductSalesSummaryUseCase =
        GetProductSalesSummaryUseCase(productSalesSummaryRepository, productRepository)
}