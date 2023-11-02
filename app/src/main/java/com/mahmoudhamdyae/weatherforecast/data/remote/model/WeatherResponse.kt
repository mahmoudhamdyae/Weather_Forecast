package com.mahmoudhamdyae.weatherforecast.data.remote.model

data class WeatherResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val current: Current,
    val hourly: List<Hourly>,
    val daily: List<Daily>,
)