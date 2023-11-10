package com.mahmoudhamdyae.weatherforecast.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.domain.repository.Repository
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class MapViewModelFactory(
    private val repository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repository) as T
    }
}

class MapViewModel (
    private val repository: Repository
): ViewModel() {

    fun addFav(location: Location) {
        viewModelScope.launch {
            repository.insertLocation(location)
        }
    }
}