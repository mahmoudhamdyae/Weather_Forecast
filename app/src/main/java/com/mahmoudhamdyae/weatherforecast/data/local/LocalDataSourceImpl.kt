package com.mahmoudhamdyae.weatherforecast.data.local

import com.mahmoudhamdyae.weatherforecast.data.local.model.Alarm
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(
    private val database: AppDatabase
): LocalDataSource {

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(database: AppDatabase): LocalDataSource {
            return INSTANCE ?: synchronized(this) {
                LocalDataSourceImpl(database).also {
                    INSTANCE = it
                }
            }
        }
    }

    override suspend fun getLocations(): Flow<List<Location>> {
        return database.locationDao().getLocations()
    }

    override suspend fun insertLocation(location: Location) {
        database.locationDao().insertLocation(location)
    }

    override suspend fun delLocation(location: Location) {
        return database.locationDao().delete(location)
    }

    override fun getAlarms(): Flow<List<Alarm>> {
        return database.alarmDao().getAlarms()
    }

    override suspend fun insertAlarm(alarm: Alarm) {
        database.alarmDao().insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        database.alarmDao().delete(alarm)
    }

    override fun getWeather(): Flow<WeatherInfo> {
        return database.weatherDao().getWeather()
    }

    override suspend fun insertWeather(weather: WeatherInfo) {
        database.weatherDao().insertWeather(weather)
    }

    override suspend fun deleteWeather(weather: WeatherInfo) {
        database.weatherDao().delete(weather)
    }
}