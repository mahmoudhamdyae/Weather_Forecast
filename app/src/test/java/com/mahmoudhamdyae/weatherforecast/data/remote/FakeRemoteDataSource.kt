package com.mahmoudhamdyae.weatherforecast.data.remote

import com.mahmoudhamdyae.weatherforecast.data.remote.model.Current
import com.mahmoudhamdyae.weatherforecast.data.remote.model.WeatherResponse

class FakeRemoteDataSource: RemoteDataSource {

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        windSpeed: String,
        language: String
    ): WeatherResponse {
        return WeatherResponse(
            31.3,
            33.3,
            "timeZone",
            4,
            Current(
                1,
                2.2,
                3,
                3.1,
                1,
                5,
                5,
                2,
                43.2,
                12.3,
                3,
                listOf(),
                3,
                32.2,
                2.1
            ),
            listOf(),
            listOf()
        )
    }
}