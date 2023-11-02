package com.mahmoudhamdyae.weatherforecast.di

import com.mahmoudhamdyae.weatherforecast.data.repository.RepositoryImpl
import com.mahmoudhamdyae.weatherforecast.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(impl: RepositoryImpl): Repository
}