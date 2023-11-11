package com.mahmoudhamdyae.weatherforecast.data.remote

import com.mahmoudhamdyae.weatherforecast.common.Constants
import com.mahmoudhamdyae.weatherforecast.data.remote.model.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceImpl private constructor(
    private val apiService: ApiService
): RemoteDataSource {

    companion object {
        @Volatile
        private var INSTANCE: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val apiService = retrofit.create(ApiService::class.java)

            return INSTANCE ?: synchronized(this) {
                RemoteDataSourceImpl(apiService).also {
                   INSTANCE = it
                }
            }
        }
    }

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        units: String,
        language: String
    ): WeatherResponse {
        return apiService.getWeather(lat, lon, units, language)
    }
}