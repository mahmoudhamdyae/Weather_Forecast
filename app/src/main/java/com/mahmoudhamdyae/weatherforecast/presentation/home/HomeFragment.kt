package com.mahmoudhamdyae.weatherforecast.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var todayAdapter: TodayAdapter
    private lateinit var nextDaysAdapter: NextDaysAdapter
    private lateinit var geoCoder: Geocoder
    private var lat = 0.0
    private var lon = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val time = LocalDateTime.now()
        val textStyle = TextStyle.SHORT
        val local = Locale.getDefault()
        binding.time.text = "${time.dayOfWeek.getDisplayName(textStyle, local)},${time.dayOfMonth} ${time.month.getDisplayName(textStyle, local)}"

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

        binding.swipe.setOnRefreshListener {
            if (lat != 0.0 && lon != 0.0) {
                viewModel.getWeather(lat, lon, requireContext())
                binding.swipe.isRefreshing = false
            }
        }
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        if (
            it[Manifest.permission.ACCESS_COARSE_LOCATION] == true ||
            it[Manifest.permission.ACCESS_FINE_LOCATION] == true
        ) {
            getLocation()
        } else {
            onLocationPermissionsNotGranted()
        }
    }

    private fun requestLocationPermissions() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun requestNetworkLocalData() {
        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mFusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        mFusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lat = location.latitude
                lon = location.longitude
                binding.viewModel?.getWeather(lat, lon, requireContext())

                try {
                    val addresses = geoCoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )
                    if (addresses?.size!! > 0) {
                        println(addresses[0].locality)
                        binding.locationName.text = addresses[0].locality
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun onLocationPermissionsNotGranted() {
        Toast.makeText(requireContext(), getString(R.string.grant_permissions_toast), Toast.LENGTH_SHORT).show()
    }
}