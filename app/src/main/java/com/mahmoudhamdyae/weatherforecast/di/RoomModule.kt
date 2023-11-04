package com.mahmoudhamdyae.weatherforecast.di

import android.content.Context
import androidx.room.Room
import com.mahmoudhamdyae.weatherforecast.data.local.AppDatabase
import com.mahmoudhamdyae.weatherforecast.data.local.model.LocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "weather_database"
        ).build()
    }

//    @Singleton
//    @Provides
//    fun provideDao(appDatabase: AppDatabase): WeatherDao {
//        return appDatabase.dao()
//    }

    @Singleton
    @Provides
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao {
        return appDatabase.locationDao()
    }
}