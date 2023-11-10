package com.mahmoudhamdyae.weatherforecast.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.weatherforecast.data.local.model.LocationDao
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val dao: LocationDao
): ViewModel() {

    private var _fav = MutableStateFlow(listOf<Location>())
    val fav = _fav.asStateFlow()

    init {
        getFav()
    }

    private fun getFav() {
        viewModelScope.launch {
            dao.getLocations().collect {
                _fav.value = it
            }
        }
    }

    fun delFav(location: Location) {
        viewModelScope.launch {
            dao.delete(location)
        }
    }
}