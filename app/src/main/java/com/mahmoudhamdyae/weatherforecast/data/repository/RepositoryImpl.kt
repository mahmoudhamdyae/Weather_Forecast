package com.mahmoudhamdyae.weatherforecast.data.repository

import com.mahmoudhamdyae.weatherforecast.data.mappers.toWeatherInfo
import com.mahmoudhamdyae.weatherforecast.data.remote.ApiService
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo
import com.mahmoudhamdyae.weatherforecast.domain.repository.Repository
import com.mahmoudhamdyae.weatherforecast.domain.util.ApiState
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService
): Repository {

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        windSpeed: String,
        language: String
    ): ApiState<WeatherInfo> {
        return try {
            ApiState.Success(
                data = apiService.getWeather(
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
}