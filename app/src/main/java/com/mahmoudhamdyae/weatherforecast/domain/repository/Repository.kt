package com.mahmoudhamdyae.weatherforecast.domain.repository

import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo
import com.mahmoudhamdyae.weatherforecast.domain.util.ApiState

interface Repository {

    suspend fun getWeather(
        lat: Double,
        lon: Double,
        windSpeed: String,
        language: String
    ): ApiState<WeatherInfo>
}