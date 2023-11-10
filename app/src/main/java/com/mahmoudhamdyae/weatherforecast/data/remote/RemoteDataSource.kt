package com.mahmoudhamdyae.weatherforecast.data.remote

import com.mahmoudhamdyae.weatherforecast.data.remote.model.WeatherResponse

interface RemoteDataSource {
    suspend fun getWeather(
        lat: Double,
        lon: Double,
        windSpeed: String,
        language: String,
    ): WeatherResponse
}