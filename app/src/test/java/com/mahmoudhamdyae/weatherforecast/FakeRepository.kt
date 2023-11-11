package com.mahmoudhamdyae.weatherforecast

import com.mahmoudhamdyae.weatherforecast.data.local.model.Alarm
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo
import com.mahmoudhamdyae.weatherforecast.domain.repository.Repository
import com.mahmoudhamdyae.weatherforecast.domain.util.ApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository: Repository {

    private val locations = mutableListOf<Location>()
    private val alarms = mutableListOf<Alarm>()

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        units: String,
        language: String
    ): ApiState<WeatherInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocations(): Flow<List<Location>> {
        return flow { emit(locations) }
    }

    override suspend fun insertLocation(location: Location) {
        locations.add(location)
    }

    override suspend fun delLocation(location: Location) {
        locations.remove(location)
    }

    override fun getAlarms(): Flow<List<Alarm>> {
        return flow { emit(alarms) }
    }

    override suspend fun insertAlarm(alarm: Alarm) {
        alarms.add(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarms.remove(alarm)
    }
}