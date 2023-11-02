package com.mahmoudhamdyae.weatherforecast.domain.model

data class WeatherDaily(
    val minTemp: Int,
    val maxTemp: Int,
    val day: String,
    val weatherDesc: String,
    val icon: String
)
