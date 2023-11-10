package com.mahmoudhamdyae.weatherforecast.data.local.dao

import androidx.room.*
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    fun getLocations(): Flow<List<Location>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location)

    @Delete
    suspend fun delete(location: Location)
}