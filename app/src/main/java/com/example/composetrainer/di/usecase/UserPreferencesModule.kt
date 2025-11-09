package com.example.composetrainer.di.usecase

import com.example.composetrainer.domain.repository.UserPreferencesRepository
import com.example.composetrainer.domain.usecase.userpreferences.GetStockRunoutLimitUseCase
import com.example.composetrainer.domain.usecase.userpreferences.SaveStockRunoutLimitUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserPreferencesModule {

    @Provides
    @Singleton
    fun provideGetStockRunoutLimitUseCase(
        repository: UserPreferencesRepository
    ): GetStockRunoutLimitUseCase = GetStockRunoutLimitUseCase(repository)


    @Provides
    @Singleton
    fun provideSaveStockRunoutLimitUseCase(
        repository: UserPreferencesRepository
    ): SaveStockRunoutLimitUseCase = SaveStockRunoutLimitUseCase(repository)
}