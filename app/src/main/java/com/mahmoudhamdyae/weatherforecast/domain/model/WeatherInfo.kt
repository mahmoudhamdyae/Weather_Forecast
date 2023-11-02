package com.mahmoudhamdyae.weatherforecast.domain.model

data class WeatherInfo(
    val weatherDataPerDay: Map<Int, List<WeatherData>>,
    val daily: List<WeatherDaily>,
    val currentWeatherData: WeatherData?
)