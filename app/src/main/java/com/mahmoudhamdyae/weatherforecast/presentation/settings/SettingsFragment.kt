package com.mahmoudhamdyae.weatherforecast.presentation.settings

import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.mahmoudhamdyae.weatherforecast.R

private const val TAG = "Preferences"

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Notifications
        findPreference<Preference>("notifications")
            ?.setOnPreferenceChangeListener { _, newValue ->
                Log.d(TAG, "Notifications enabled: $newValue")
                true
            }

        // Location
        findPreference<Preference>("location")
            ?.setOnPreferenceChangeListener { _, newValue ->
                Log.d(TAG, "Notifications enabled: $newValue")
                true
            }

        // Temperature
        findPreference<Preference>("wind_speed")
            ?.setOnPreferenceChangeListener { _, newValue ->
                Log.d(TAG, "Temp: $newValue")
                true
            }

        // Wind Speed
        findPreference<Preference>("location")
            ?.setOnPreferenceChangeListener { _, newValue ->
                Log.d(TAG, "Wind Speed: $newValue")
                true
            }

        // Language
        findPreference<Preference>("language")
            ?.setOnPreferenceChangeListener { _, newValue ->
                Log.d(TAG, "Language: $newValue")
                true
            }

        // Feedback
        findPreference<Preference>("feedback")
            ?.setOnPreferenceClickListener {
                Log.d(TAG, "Feedback was clicked")
                true
            }

















        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext()).all

        preferences.forEach {
            Log.d("Preferences", "${it.key} -> ${it.value}")
        }
    }
}