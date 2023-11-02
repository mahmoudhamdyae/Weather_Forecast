package com.mahmoudhamdyae.weatherforecast.data.mappers

import com.mahmoudhamdyae.weatherforecast.data.remote.model.Hourly
import com.mahmoudhamdyae.weatherforecast.data.remote.model.WeatherResponse
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherDaily
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherData
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

fun List<Hourly>.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    return mapIndexed { index, hourly ->
        val temperature = hourly.temp
        val windSpeed = hourly.wind_speed
        val pressure = hourly.pressure
        val humidity = hourly.humidity
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = dateTime(hourly.dt),
                temperatureCelsius = temperature.toInt(),
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                weatherDesc = hourly.weather[0].description,
                icon = hourly.weather[0].icon
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map { it.data }
    }
}

private fun dateTime(time: Int): LocalDateTime {
    return Instant.ofEpochSecond(time.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

private fun WeatherResponse.toDailyWeather(): List<WeatherDaily> {
    val dailyWeather = mutableListOf<WeatherDaily>()
    daily.forEach {
        if (LocalDateTime.now().dayOfWeek != dateTime(it.dt).dayOfWeek) {
            dailyWeather.add(
                WeatherDaily(
                    minTemp = it.temp.min.toInt(),
                    maxTemp = it.temp.max.toInt(),
                    weatherDesc = it.weather[0].description,
                    icon = it.weather[0].icon,
                    day = if (
                        LocalDateTime.now().dayOfWeek + 1 == dateTime(it.dt).dayOfWeek
                    ) {
                        "Tomorrow"
                    } else {
                        dateTime(it.dt).dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    },
                )
            )
        }
    }
    return dailyWeather
}

fun WeatherResponse.toWeatherInfo(): WeatherInfo {
    val weatherDataPerDay = hourly.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataPerDay[0]?.find {
        val hour = if (now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }
    val dailyWeather = toDailyWeather()
    return WeatherInfo(
        weatherDataPerDay = weatherDataPerDay,
        currentWeatherData = currentWeatherData,
        daily = dailyWeather
    )
}