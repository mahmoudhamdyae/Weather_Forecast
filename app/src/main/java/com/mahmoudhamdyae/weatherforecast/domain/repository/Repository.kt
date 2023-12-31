package com.mahmoudhamdyae.weatherforecast.domain.repository

import com.mahmoudhamdyae.weatherforecast.data.local.model.Alarm
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo
import com.mahmoudhamdyae.weatherforecast.domain.util.ApiState
import kotlinx.coroutines.flow.Flow

interface Repository {

    // Weather
    suspend fun getWeather(
        lat: Double,
        lon: Double,
        units: String,
        language: String
    ): ApiState<WeatherInfo>

    // Locations
    suspend fun getLocations(): Flow<List<Location>>
    suspend fun insertLocation(location: Location)
    suspend fun delLocation(location: Location)

    // Alarms
    fun getAlarms(): Flow<List<Alarm>>
    suspend fun insertAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
}