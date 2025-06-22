package com.mahmoudhamdyae.weatherforecast.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mahmoudhamdyae.weatherforecast.common.Converters
import java.util.*

@Entity
@TypeConverters(Converters::class)
data class WeatherInfo(
    val weatherDataPerDay: Map<Int, List<WeatherData>>,
    val daily: List<WeatherDaily>,
    val currentWeatherData: WeatherData?,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)