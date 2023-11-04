package com.mahmoudhamdyae.weatherforecast.domain.model

data class Alarm(
    val fromMin: Int,
    val fromHour: Int,
    val fromDay: Int,
    val fromMonth: Int,
    val fromYear: Int,
    val toMin: Int,
    val toHour: Int,
    val toDay: Int,
    val toMonth: Int,
    val toYear: Int,
    val isAlarm: Boolean
)
