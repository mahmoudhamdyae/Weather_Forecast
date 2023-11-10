package com.mahmoudhamdyae.weatherforecast.data.local

import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(
    private val dao: LocationDao
): LocalDataSource {

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(dao: LocationDao): LocalDataSource {
            return INSTANCE ?: synchronized(this) {
                LocalDataSourceImpl(dao).also {
                    INSTANCE = it
                }
            }
        }
    }

    override suspend fun getLocations(): Flow<List<Location>> {
        return dao.getLocations()
    }

    override suspend fun insertLocation(location: Location) {
        dao.insertLocation(location)
    }

    override suspend fun delLocation(location: Location) {
        return dao.delete(location)
    }
}