package com.demo.advanced.practical.di.module

import com.demo.advanced.practical.di.qualifier.BaseApiService
import com.demo.advanced.practical.repository.MainRepository
import com.demo.advanced.practical.restfullapi.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRepositoryModule(
        @BaseApiService apiService: ApiService
    ): MainRepository =
        MainRepository(apiService)
}