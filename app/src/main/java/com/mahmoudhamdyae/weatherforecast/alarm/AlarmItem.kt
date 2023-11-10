package com.mahmoudhamdyae.weatherforecast.alarm

import java.time.LocalDateTime

data class AlarmItem(
    val alarmTime : LocalDateTime,
    val message : String,
    val alarmType: AlarmType
)