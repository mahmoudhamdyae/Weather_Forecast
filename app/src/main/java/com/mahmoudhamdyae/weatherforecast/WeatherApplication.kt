package com.mahmoudhamdyae.weatherforecast

import android.app.Application
import com.google.android.material.color.DynamicColors

class WeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}