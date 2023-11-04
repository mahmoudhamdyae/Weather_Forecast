package com.mahmoudhamdyae.weatherforecast.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.mahmoudhamdyae.weatherforecast.R

class InitialSetupDialogFragment: DialogFragment() {

    private lateinit var switch: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_initial_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        PreferenceManager.setDefaultValues(requireContext(), R.xml.preferences, true)

        val radioGroup: RadioGroup = view.findViewById(R.id.location_radio_group)
        radioGroup.check(R.id.location_gps)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.location_gps) {
                sp.edit().putString("location", requireContext().getString(R.string.pref_location_gps)).apply()
            } else {
                sp.edit().putString("location", requireContext().getString(R.string.pref_location_map)).apply()
            }
        }
        switch = view.findViewById(R.id.notifications_switch)
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission()
                } else {
                    sp.edit().putBoolean("notifications", true).apply()
                }
            } else {
                sp.edit().putBoolean("notifications", false).apply()
            }
        }

        view.findViewById<Button>(R.id.ok_button).setOnClickListener { dismiss() }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
            val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
            sp.edit().putBoolean("notifications", true).apply()
        } else {
            switch.isChecked = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (checkNotificationPermission()) {
            // FCM SDK (and your app) can post notifications.
        } else if (!shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            // Directly ask for the permission
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun checkNotificationPermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

    companion object {
        const val TAG = "InitialSetupDialogFragment"
    }
}