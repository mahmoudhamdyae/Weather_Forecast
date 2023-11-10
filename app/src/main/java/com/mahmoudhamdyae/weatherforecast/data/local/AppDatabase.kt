package com.mahmoudhamdyae.weatherforecast.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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