package com.smart.htu.di

import com.smart.htu.api.network.AppService
import com.smart.htu.api.network.JWCService
import com.smart.htu.repo.DataStoreRepo
import com.smart.htu.repo.SharedDataRepoImpl
import com.smart.htu.repo.SharedDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SharedDataModule {

    @Provides
    @Singleton
    fun provideSharedRepository(
        jwcService: JWCService,
        appService: AppService,
        dataStoreRepo: DataStoreRepo
    ): SharedDataRepository {
        return SharedDataRepoImpl(jwcService, appService, dataStoreRepo)
    }

}