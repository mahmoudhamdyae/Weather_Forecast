package com.mahmoudhamdyae.weatherforecast.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weatherInfo")
    suspend fun getWeather(): List<WeatherInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherInfo)

    @Query("DELETE FROM weatherInfo")
    suspend fun delete()
}