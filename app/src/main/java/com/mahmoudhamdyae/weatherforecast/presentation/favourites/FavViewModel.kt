package com.mahmoudhamdyae.weatherforecast.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.domain.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class FavViewModelFactory(
    private val repository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavViewModel(repository) as T
    }
}

class FavViewModel (
    private val repository: Repository
): ViewModel() {

    private var _fav = MutableStateFlow(listOf<Location>())
    val fav = _fav.asStateFlow()

    init {
        getFavList()
    }

    fun getFavList() {
        viewModelScope.launch {
            repository.getLocations().collect {
                _fav.value = it
            }
        }
    }

    fun delFav(location: Location) {
        viewModelScope.launch {
            repository.delLocation(location)
        }
    }

    fun addFav(location: Location) {
        viewModelScope.launch {
            repository.insertLocation(location)
        }
    }
}