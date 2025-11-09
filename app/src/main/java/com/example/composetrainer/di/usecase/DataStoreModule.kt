package com.example.composetrainer.di.usecase

import android.content.Context
import com.example.composetrainer.data.local.datastore.userPreferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    // Provide DataStore<Preferences> instance
    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(@ApplicationContext context: Context) =
        context.userPreferencesDataStore
}