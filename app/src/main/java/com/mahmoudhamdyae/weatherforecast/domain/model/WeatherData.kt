package com.mahmoudhamdyae.weatherforecast.domain.model

import java.time.LocalDateTime

data class WeatherData(
    val time: LocalDateTime,
    val temperatureCelsius: Int,
    val pressure: Int,
    val windSpeed: Double,
    val humidity: Int,
    val weatherDesc: String,
    val icon: String
)
