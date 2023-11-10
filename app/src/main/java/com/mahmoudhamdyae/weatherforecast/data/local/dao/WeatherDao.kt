package com.mahmoudhamdyae.weatherforecast.data.local.dao

import androidx.room.*
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weatherInfo")
    fun getWeather(): Flow<WeatherInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherInfo)

    @Delete
    suspend fun delete(weather: WeatherInfo)
}