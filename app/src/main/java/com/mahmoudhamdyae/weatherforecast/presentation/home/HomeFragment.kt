package com.mahmoudhamdyae.weatherforecast.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.data.SharedPrefImpl
import com.mahmoudhamdyae.weatherforecast.data.local.AppDatabase
import com.mahmoudhamdyae.weatherforecast.data.local.LocalDataSourceImpl
import com.mahmoudhamdyae.weatherforecast.data.remote.RemoteDataSourceImpl
import com.mahmoudhamdyae.weatherforecast.data.repository.RepositoryImpl
import com.mahmoudhamdyae.weatherforecast.databinding.FragmentHomeBinding
import com.mahmoudhamdyae.weatherforecast.presentation.map.LATITUDE
import com.mahmoudhamdyae.weatherforecast.presentation.map.LONGITUDE
import com.mahmoudhamdyae.weatherforecast.presentation.map.NAME
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var todayAdapter: TodayAdapter
    private lateinit var nextDaysAdapter: NextDaysAdapter
    private lateinit var geoCoder: Geocoder
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var name: String? = null
    private var isGps: Boolean = true

    private val viewModel: HomeViewModel by lazy {
        val factory = HomeViewModelFactory(
            RepositoryImpl.getRepository(
                RemoteDataSourceImpl.getInstance(),
                LocalDataSourceImpl.getInstance(
                    AppDatabase.getDatabase(requireContext())
                )
            )
        )
        ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getLocation()
        viewModel.getWeather(lat, lon, requireContext())
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext()).all
        isGps = preferences["location"] == getString(R.string.pref_location_gps)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        todayAdapter = TodayAdapter()
        nextDaysAdapter = NextDaysAdapter()
        binding.todayAdapter = todayAdapter
        binding.nextDaysAdapter = nextDaysAdapter

        val intent = activity?.intent
        lat = intent?.getDoubleExtra(LATITUDE, 0.0) ?: 0.0
        lon = intent?.getDoubleExtra(LONGITUDE, 0.0) ?: 0.0
        name = intent?.getStringExtra(NAME)
        if (name != null) binding.locationName.text = name

        geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val time = LocalDateTime.now()
        val textStyle = TextStyle.SHORT
        val local = Locale.getDefault()
        binding.time.text = "${time.dayOfWeek.getDisplayName(textStyle, local)},${time.dayOfMonth} ${time.month.getDisplayName(textStyle, local)}"

        if (isGps) checkLocationPermissions()
        if (lat == 0.0 || lon == 0.0) {
            if (isGps) {
                getLocation()
            } else {
                lat = SharedPrefImpl.getInstance(requireContext()).readLatAndLon().first ?: 0.0
                lon = SharedPrefImpl.getInstance(requireContext()).readLatAndLon().second ?: 0.0
            }
        } else {
            viewModel.getWeather(lat, lon, requireContext())
        }

        binding.swipe.setOnRefreshListener {
            if (lat != 0.0 && lon != 0.0) {
                viewModel.getWeather(lat, lon, requireContext())
                binding.swipe.isRefreshing = false
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (!isNetworkAvailable(requireContext())) {
                    Toast.makeText(requireContext(), R.string.no_connection_toast, Toast.LENGTH_SHORT).show()
                }
                todayAdapter.submitList(it.weather?.weatherDataPerDay?.get(0))
                nextDaysAdapter.submitList(it.weather?.daily)

                binding.currentTime.text = view.context.getString(R.string.today, "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}")

            }
        }
        if (SharedPrefImpl.getInstance(requireContext()).isFirstTime()) {
            showInitialSetupDialog()
        }
    }

    private fun showInitialSetupDialog() {
        val dialogFragment = InitialSetupDialogFragment()
        dialogFragment.isCancelable = false
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        dialogFragment.show(transaction!!, InitialSetupDialogFragment.TAG)
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
        if (isGps) {
            if (checkLocationPermissions()) {
                if (isLocationEnabled()) {
                    requestNetworkLocalData()
                } else {
                    Toast.makeText(requireContext(), "Turn on Location", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                requestLocationPermissions()
            }
        } else {
            lat = SharedPrefImpl.getInstance(requireContext()).readLatAndLon().first ?: 0.0
            lon = SharedPrefImpl.getInstance(requireContext()).readLatAndLon().second ?: 0.0
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
                        binding.locationName.text = addresses[0].locality
                    } else {
                        binding.locationName.text = ""
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

    @SuppressLint("MissingPermission")
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        return connectivityManager.run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            } ?: false
        }
    }
}