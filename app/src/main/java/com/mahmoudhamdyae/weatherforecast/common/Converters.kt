package com.mahmoudhamdyae.weatherforecast.common

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherDaily
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherData

object Converters {

    // List

    @TypeConverter
    fun listFromString(value: String): List<WeatherDaily> {
        val listType = object : TypeToken<List<WeatherDaily>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun listToString(value: List<WeatherDaily>): String {
        return Gson().toJson(value)
    }

    // Map

    @TypeConverter
    fun fromString(value: String): Map<Int, List<WeatherData>> {
        val mapType = object : TypeToken<Map<Int, List<WeatherData>>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun toString(value: Map<Int, List<WeatherData>>): String {
        return Gson().toJson(value)
    }

    // Weather Data

    @TypeConverter
    fun weatherDataFromString(value: String): WeatherData? {
        val mapType = object : TypeToken<WeatherData?>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun weatherDataToString(value: WeatherData?): String {
        return Gson().toJson(value)
    }
}