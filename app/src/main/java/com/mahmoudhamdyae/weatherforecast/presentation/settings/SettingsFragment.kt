package com.mahmoudhamdyae.weatherforecast.presentation.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
                if (newValue == true) {
                    askNotificationPermission()
                }
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
            val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
            PreferenceManager.setDefaultValues(requireContext(), R.xml.preferences, true)
            sp.edit().putBoolean("notifications", true).apply()
        } else {
            val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
            PreferenceManager.setDefaultValues(requireContext(), R.xml.preferences, true)
            sp.edit().putBoolean("notifications", false).apply()

            preferenceScreen = null
            addPreferencesFromResource(R.xml.preferences)
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}