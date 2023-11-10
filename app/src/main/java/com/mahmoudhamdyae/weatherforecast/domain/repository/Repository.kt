package com.mahmoudhamdyae.weatherforecast.domain.repository

import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo
import com.mahmoudhamdyae.weatherforecast.domain.util.ApiState
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getWeather(
        lat: Double,
        lon: Double,
        windSpeed: String,
        language: String
    ): ApiState<WeatherInfo>

    suspend fun getLocations(): Flow<List<Location>>
    suspend fun insertLocation(location: Location)
    suspend fun delLocation(location: Location)
}