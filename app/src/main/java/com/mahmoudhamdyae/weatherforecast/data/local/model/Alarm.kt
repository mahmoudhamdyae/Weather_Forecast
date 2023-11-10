package com.mahmoudhamdyae.weatherforecast.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Alarm(
    val minute: Int,
    val hour: Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val isAlarm: Boolean,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)
