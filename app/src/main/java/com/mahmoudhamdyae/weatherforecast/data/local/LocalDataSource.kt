package com.mahmoudhamdyae.weatherforecast.data.local

import com.mahmoudhamdyae.weatherforecast.data.local.model.Alarm
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getLocations(): Flow<List<Location>>
    suspend fun insertLocation(location: Location)
    suspend fun delLocation(location: Location)

    fun getAlarms(): Flow<List<Alarm>>
    suspend fun insertAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
}