package com.mahmoudhamdyae.weatherforecast.domain.repository

interface SharedPref {
    fun isFirstTime(): Boolean
    fun setFirstTimePreference()
    fun writeLatAndLon(lat: Double, lon: Double)
    fun readLatAndLon(): Pair<Double?, Double?>
}