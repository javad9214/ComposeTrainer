package com.example.composetrainer.di


import com.example.composetrainer.data.remote.api.ApiServiceVersion
import com.example.composetrainer.data.remote.datasource.VersionRemoteDataSource
import com.example.composetrainer.data.repository.VersionRepositoryImpl
import com.example.composetrainer.domain.repository.VersionRepository
import com.example.composetrainer.domain.usecase.versionupdate.CheckAppVersionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VersionModule {


    @Provides
    @Singleton
    fun provideVersionRemoteDataSource(
        apiService: ApiServiceVersion
    ): VersionRemoteDataSource {
        return VersionRemoteDataSource(apiService)
    }


    @Provides
    @Singleton
    fun provideVersionRepository(
        remoteDataSource: VersionRemoteDataSource
    ): VersionRepository {
        return VersionRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideCheckAppVersionUseCase(
        repository: VersionRepository
    ): CheckAppVersionUseCase {
        return CheckAppVersionUseCase(repository)
    }
}