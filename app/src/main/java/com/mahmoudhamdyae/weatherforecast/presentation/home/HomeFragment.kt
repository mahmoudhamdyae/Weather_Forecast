package com.mahmoudhamdyae.weatherforecast.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

private const val PERMISSION_Location_ID = 5005

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var todayAdapter: TodayAdapter
    private lateinit var nextDaysAdapter: NextDaysAdapter
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkLocationPermissions()
        getLocation()

        val viewModel: HomeViewModel by viewModels()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        todayAdapter = TodayAdapter()
        nextDaysAdapter = NextDaysAdapter()
        binding.todayAdapter = todayAdapter
        binding.nextDaysAdapter = nextDaysAdapter

        viewModel.weather.observe(this) {
            todayAdapter.submitList(it?.weatherDataPerDay?.get(0))
            nextDaysAdapter.submitList(it?.daily)
        }

        viewModel.isFirstTime.observe(this) {
            if (it) Toast.makeText(requireContext(), "First time", Toast.LENGTH_SHORT).show()
            else Toast.makeText(requireContext(), "Not first time", Toast.LENGTH_SHORT).show()
        }


        viewModel.getWeather(33.23, 31.32)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun checkLocationPermissions() = ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

    private fun getLocation() {
        if (checkLocationPermissions()) {
            if (isLocationEnabled()) {
                requestNetworkLocalData()
            } else {
                onLocationNotGranted()
                openLocationDialog(requireContext())
            }
        } else {
            requestLocationPermissions()
        }
    }

    private fun openLocationDialog(context: Context) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).apply {
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(requireActivity(), 100)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            PERMISSION_Location_ID
        )
    }

    private val mLocationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val mLastLocation = locationResult.lastLocation
            // todo
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_Location_ID) {
            if (
                grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                getLocation()
            } else {
                onLocationPermissionsNotGranted()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun requestNetworkLocalData() {
        val mLocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0).apply {
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private fun onLocationPermissionsNotGranted() {
        Toast.makeText(requireContext(), getString(R.string.grant_permissions_toast), Toast.LENGTH_SHORT).show()
    }

    private fun onLocationNotGranted() {
        Toast.makeText(requireContext(), getString(R.string.turn_on_location_toast), Toast.LENGTH_SHORT).show()
    }
}