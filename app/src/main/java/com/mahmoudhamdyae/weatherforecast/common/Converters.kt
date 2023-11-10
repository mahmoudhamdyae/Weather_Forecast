package com.mahmoudhamdyae.weatherforecast.common

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherDaily
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherData

//@ProvidedTypeConverter
object Converters {

//    @field:TypeConverters(Converters::class)
//    var gson = Gson()

    // Convert List

//    @TypeConverter
//    fun stringToList(data: String?): List<WeatherDaily?>? {
//        if (data == null) {
//            return Collections.emptyList()
//        }
//        val listType = object : TypeToken<List<WeatherDaily?>?>() {}.type
//        return gson.fromJson<List<WeatherDaily?>>(data, listType)
//    }
//
//    @TypeConverter
//    fun listToString(someObjects: List<WeatherDaily?>?): String? {
//        return gson.toJson(someObjects)
//    }

    // Convert Maps

//    @TypeConverter
//    fun stringToMap(data: String?): Map<Int, List<WeatherData>> {
//        if (data == null) {
//            return Collections.emptyMap()
//        }
//        val listType = object : TypeToken<Map<Int, List<WeatherData>>>() {}.type
//        return gson.fromJson(data, listType)
//    }
//
//    @TypeConverter
//    fun mapToString(someObjects: Map<Int, List<WeatherData>>): String? {
//        return gson.toJson(someObjects)
//    }

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