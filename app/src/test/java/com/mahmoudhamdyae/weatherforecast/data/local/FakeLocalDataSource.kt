package com.mahmoudhamdyae.weatherforecast.data.local

import com.mahmoudhamdyae.weatherforecast.data.local.model.Alarm
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource(
    private var weathers: MutableList<WeatherInfo> = mutableListOf(),
    private var locations: MutableList<Location> = mutableListOf(),
    private var alarms: MutableList<Alarm> = mutableListOf()
): LocalDataSource {

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

    override suspend fun getWeather(): List<WeatherInfo> {
        return weathers
    }

    override suspend fun insertWeather(weather: WeatherInfo) {
        weathers.add(weather)
    }

    override suspend fun deleteWeather() {
        weathers = mutableListOf()
    }
}