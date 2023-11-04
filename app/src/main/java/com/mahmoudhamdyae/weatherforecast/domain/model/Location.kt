package com.mahmoudhamdyae.weatherforecast.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Location(
    val lat: Double,
    val lon: Double,
    val name: String,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)
