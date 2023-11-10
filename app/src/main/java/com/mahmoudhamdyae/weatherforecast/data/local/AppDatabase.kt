package com.mahmoudhamdyae.weatherforecast.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mahmoudhamdyae.weatherforecast.common.Converters
import com.mahmoudhamdyae.weatherforecast.data.local.dao.AlarmDao
import com.mahmoudhamdyae.weatherforecast.data.local.dao.LocationDao
import com.mahmoudhamdyae.weatherforecast.data.local.dao.WeatherDao
import com.mahmoudhamdyae.weatherforecast.data.local.model.Alarm
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo

@Database(
    entities = [Location::class, Alarm::class, WeatherInfo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun locationDao(): LocationDao
    abstract fun alarmDao(): AlarmDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "weather_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}