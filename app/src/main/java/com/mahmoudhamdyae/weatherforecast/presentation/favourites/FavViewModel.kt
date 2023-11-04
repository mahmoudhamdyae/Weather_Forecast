package com.mahmoudhamdyae.weatherforecast.presentation.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.weatherforecast.data.local.model.LocationDao
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val dao: LocationDao
): ViewModel() {

    private var _fav = MutableLiveData(listOf<Location>())
    val fav: LiveData<List<Location>>
        get() = _fav

    init {
        getFav()
    }

    private fun getFav() {
        viewModelScope.launch {
            _fav.postValue(dao.getLocations())
        }
    }

    fun delFav(location: Location) {
        viewModelScope.launch {
            dao.delete(location)
        }
    }
}