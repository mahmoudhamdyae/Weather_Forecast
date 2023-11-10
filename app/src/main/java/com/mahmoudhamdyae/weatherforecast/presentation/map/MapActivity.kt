package com.mahmoudhamdyae.weatherforecast.presentation.map

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mahmoudhamdyae.weatherforecast.MainActivity
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.data.local.AppDatabase
import com.mahmoudhamdyae.weatherforecast.data.local.LocalDataSourceImpl
import com.mahmoudhamdyae.weatherforecast.data.remote.RemoteDataSourceImpl
import com.mahmoudhamdyae.weatherforecast.data.repository.RepositoryImpl
import com.mahmoudhamdyae.weatherforecast.databinding.ActivityMapBinding
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import java.io.IOException
import java.util.*

const val LATITUDE: String = "LATITUDE"
const val LONGITUDE: String = "LONGITUDE"
const val NAME: String = "NAME"

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var geoCoder: Geocoder
    private var lat: Double? = null
    private var lon: Double? = null
    private var name: String? = null

    private val viewModel: MapViewModel by lazy {
        val factory = MapViewModelFactory(
            RepositoryImpl.getRepository(
                RemoteDataSourceImpl.getInstance(),
                LocalDataSourceImpl.getInstance(
                    AppDatabase.getDatabase(this)
                )
            )
        )
        ViewModelProvider(this, factory)[MapViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.lifecycleOwner = this

        geoCoder = Geocoder(this, Locale.getDefault())

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.addThisLocationButton.setOnClickListener {
            viewModel.addFav(
                Location(
                    lat!!,
                    lon!!,
                    name!!
                )
            )
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(LATITUDE, lat)
            intent.putExtra(LONGITUDE, lon)
            intent.putExtra(NAME, name)
            startActivity(intent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(
            MarkerOptions()
            .position(sydney)
            .title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        setMapClick()
    }

    private fun setMapClick() {
        mMap.setOnMapClickListener { latLng ->
            lat = latLng.latitude
            lon = latLng.longitude
            try {
                val addresses = geoCoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1
                )
                if (addresses?.size!! > 0) {
                    name = addresses[0].locality
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            mMap.clear()
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(name)
            )
            binding.addThisLocationButton.visibility = View.VISIBLE
        }
    }
}