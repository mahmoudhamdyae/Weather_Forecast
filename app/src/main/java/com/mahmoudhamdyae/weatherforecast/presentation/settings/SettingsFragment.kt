package com.mahmoudhamdyae.weatherforecast.presentation.settings

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.mahmoudhamdyae.weatherforecast.MainActivity
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.presentation.map.MapActivity
import java.util.*


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
                Log.d(TAG, "location: $newValue")
                if (newValue == R.string.pref_location_map) {
                    openMap()
                    requireActivity().finish()
                }
                true
            }

        // Temperature
        findPreference<Preference>("temp")
            ?.setOnPreferenceChangeListener { _, newValue ->
                Log.d(TAG, "Temp: $newValue")
                true
            }

        // Wind Speed
        findPreference<Preference>("wind_speed")
            ?.setOnPreferenceChangeListener { _, newValue ->
                Log.d(TAG, "Wind Speed: $newValue")
                true
            }

        // Language
        findPreference<Preference>("language")
            ?.setOnPreferenceChangeListener { _, newValue ->
                Log.d(TAG, "Language: $newValue")
                setLanguage(newValue.toString())
                true
            }

        // Feedback
        findPreference<Preference>("feedback")
            ?.setOnPreferenceClickListener {
                Log.d(TAG, "Feedback was clicked")

                val mailto = "mailto:mahmoudhamdyae@gmail.com" +
                        "?subject=" + "Weather forecast feedback"
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse(mailto)

                try {
                    startActivity(emailIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(requireContext(), "There is no email app available", Toast.LENGTH_SHORT).show()
                }
                true
            }
    }

    private fun setLanguage(language: String) {
        val lang = if (language == getString(R.string.pref_language_english)) {
            "en"
        } else {
            "ar"
        }

        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(lang))
        conf.setLayoutDirection(Locale(lang))
        res.updateConfiguration(conf, dm)
        restartApp()
    }

    private fun restartApp() {
        val intent = Intent(context, MainActivity::class.java)
        this.startActivity(intent)
        requireActivity().finishAffinity()
    }

    private fun openMap() {
        val intent = Intent(requireActivity(), MapActivity::class.java)
        startActivity(intent)
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