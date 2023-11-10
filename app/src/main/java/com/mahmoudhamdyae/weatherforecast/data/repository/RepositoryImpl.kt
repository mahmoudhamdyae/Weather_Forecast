package com.mahmoudhamdyae.weatherforecast.data.repository

import com.mahmoudhamdyae.weatherforecast.data.local.LocalDataSource
import com.mahmoudhamdyae.weatherforecast.data.mappers.toWeatherInfo
import com.mahmoudhamdyae.weatherforecast.data.remote.RemoteDataSource
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo
import com.mahmoudhamdyae.weatherforecast.domain.repository.Repository
import com.mahmoudhamdyae.weatherforecast.domain.util.ApiState
import kotlinx.coroutines.flow.Flow

class RepositoryImpl (
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): Repository {

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getRepository(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource
        ): Repository {
            return INSTANCE ?: synchronized(this) {
                RepositoryImpl(remoteDataSource, localDataSource).also {
                    INSTANCE = it
                }
            }
        }
    }

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        windSpeed: String,
        language: String
    ): ApiState<WeatherInfo> {
        return try {
            ApiState.Success(
                data = remoteDataSource.getWeather(
                    lat = lat,
                    lon = lon,
                    windSpeed = windSpeed,
                    language = language
                ).toWeatherInfo()
            )
        } catch(e: Exception) {
            e.printStackTrace()
            ApiState.Failure(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun getLocations(): Flow<List<Location>> {
        return localDataSource.getLocations()
    }

    override suspend fun insertLocation(location: Location) {
        localDataSource.insertLocation(location)
    }

    override suspend fun delLocation(location: Location) {
        localDataSource.delLocation(location)
    }
}