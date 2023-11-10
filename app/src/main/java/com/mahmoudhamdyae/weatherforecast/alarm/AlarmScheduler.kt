package com.mahmoudhamdyae.weatherforecast.alarm

interface AlarmScheduler {
    fun schedule(alarmItem: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}