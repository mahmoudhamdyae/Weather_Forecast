package com.mahmoudhamdyae.weatherforecast.data.local.dao

import androidx.room.*
import com.mahmoudhamdyae.weatherforecast.data.local.model.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM Alarm")
    fun getAlarms(): Flow<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)
}