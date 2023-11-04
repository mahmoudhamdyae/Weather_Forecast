package com.mahmoudhamdyae.weatherforecast.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mahmoudhamdyae.weatherforecast.common.Converters
import com.mahmoudhamdyae.weatherforecast.data.local.model.LocationDao
import com.mahmoudhamdyae.weatherforecast.domain.model.Location

@Database(
//    entities = [RoomWeather::class],
    entities = [Location::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
//    abstract fun dao(): WeatherDao
    abstract fun locationDao(): LocationDao
}