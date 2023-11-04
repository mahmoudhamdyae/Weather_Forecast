package com.mahmoudhamdyae.weatherforecast.data.local.model

import androidx.room.*
import com.mahmoudhamdyae.weatherforecast.domain.model.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    suspend fun getLocations(): List<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location)

    @Delete
    suspend fun delete(location: Location)
}