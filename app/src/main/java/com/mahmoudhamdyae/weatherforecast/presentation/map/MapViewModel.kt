package com.mahmoudhamdyae.weatherforecast.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.weatherforecast.data.local.model.LocationDao
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val dao: LocationDao
): ViewModel() {

    fun addFav(location: Location) {
        viewModelScope.launch {
            dao.insertLocation(location)
        }
    }
}