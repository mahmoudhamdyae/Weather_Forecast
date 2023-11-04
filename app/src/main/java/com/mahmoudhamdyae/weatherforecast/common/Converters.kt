package com.mahmoudhamdyae.weatherforecast.common

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherDaily
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherData
import java.util.*

@ProvidedTypeConverter
object Converters {

    @field:TypeConverters(Converters::class)
    var gson = Gson()

    // Convert List

    @TypeConverter
    fun stringToList(data: String?): List<WeatherDaily?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<WeatherDaily?>?>() {}.type
        return gson.fromJson<List<WeatherDaily?>>(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<WeatherDaily?>?): String? {
        return gson.toJson(someObjects)
    }

    // Convert Maps

    @TypeConverter
    fun stringToMap(data: String?): Map<Int, List<WeatherData>> {
        if (data == null) {
            return Collections.emptyMap()
        }
        val listType = object : TypeToken<Map<Int, List<WeatherData>>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun mapToString(someObjects: Map<Int, List<WeatherData>>): String? {
        return gson.toJson(someObjects)
    }
}