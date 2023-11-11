package com.mahmoudhamdyae.weatherforecast.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.mahmoudhamdyae.weatherforecast.domain.repository.SharedPref

class SharedPrefImpl private constructor(
    private val context: Context,
) : SharedPref {

    companion object {
        private const val PREFS_NAME = "MyPrefsFile"
        private const val LAT_KEY = "LAT_KEY"
        private const val LON_KEY = "LON_KEY"
        private const val IS_FIRST_TIME = "IS_FIRST_TIME"

        @Volatile
        private var INSTANCE: SharedPref? = null

        fun getInstance(context: Context): SharedPref {
            return INSTANCE ?: synchronized(this) {
                SharedPrefImpl(context).also {
                    INSTANCE = it
                }
            }
        }
    }

    override fun isFirstTime(): Boolean {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean(IS_FIRST_TIME, true)
        setFirstTimePreference()
        return isFirstTime
    }

    private fun setFirstTimePreference() {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_FIRST_TIME, false)
        editor.apply()
    }

    override fun writeLatAndLon(lat: Double, lon: Double) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat(LAT_KEY, lat.toFloat())
        editor.putFloat(LON_KEY, lon.toFloat())
        editor.apply()
        Log.i("hahahahahaha", "writeLatAndLon: $lat $lon")
    }

    override fun readLatAndLon(): Pair<Double?, Double?> {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lat = sharedPreferences.getFloat(LAT_KEY, 0f)
        val lon = sharedPreferences.getFloat(LON_KEY, 0f)
        return Pair(lat.toDouble(), lon.toDouble())
    }
}