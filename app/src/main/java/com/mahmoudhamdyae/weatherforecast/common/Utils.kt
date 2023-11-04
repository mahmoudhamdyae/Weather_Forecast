package com.mahmoudhamdyae.weatherforecast.common

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun dateTime(time: Int): LocalDateTime {
    return Instant.ofEpochSecond(time.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}