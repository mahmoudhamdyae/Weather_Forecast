package com.mahmoudhamdyae.weatherforecast.data.remote

import com.mahmoudhamdyae.weatherforecast.common.Constants
import com.mahmoudhamdyae.weatherforecast.data.remote.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("onecall?appid=${Constants.API_KEY}")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") windSpeed: String,
        @Query("lang") language: String,
    ): WeatherResponse
}