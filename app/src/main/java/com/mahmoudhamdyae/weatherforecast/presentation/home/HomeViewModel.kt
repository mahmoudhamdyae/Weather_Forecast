package com.mahmoudhamdyae.weatherforecast.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo
import com.mahmoudhamdyae.weatherforecast.domain.repository.PreferencesRepository
import com.mahmoudhamdyae.weatherforecast.domain.repository.Repository
import com.mahmoudhamdyae.weatherforecast.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    preferencesRepository: PreferencesRepository,
    private val repository: Repository
): ViewModel() {

    private var _isFirstTime = MutableLiveData<Boolean>()
    val isFirstTime: LiveData<Boolean>
        get() = _isFirstTime

    private var _weather = MutableLiveData<WeatherInfo?>()
    val weather: LiveData<WeatherInfo?>
        get() = _weather

    init {
        viewModelScope.launch {
            preferencesRepository.isFirstTime.collect {
                if (it) {
                    // First Time
                    // Navigate to OnBoarding Screen
                    _isFirstTime.postValue(true)
                    this.cancel()
                } else {
                    this.cancel()
                }
            }
        }
    }

    fun getWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
//            preferences.readLatitude.collect { latitude ->
//                preferences.readLongitude.collect { longitude ->

//                    if (latitude != null && longitude != null) {
//                        _uiState.update {
//                            WeatherUiState(isLoading = true, error = null)
//                        }
//                        when (val result = repository.getWeather(latitude, longitude)) {
            when (val result = repository.getWeather(lat, lon)) {
                is Resource.Success -> {
                    _weather.postValue(result.data)
//                                _uiState.update {
//                                    WeatherUiState(
//                                        weatherInfo = result.data,
//                                        isLoading = false,
//                                        error = null
//                                    )
//                                }
                }
                is Resource.Error -> {
//                                _uiState.update {
//                                    WeatherUiState(
//                                        weatherInfo = null,
//                                        isLoading = false,
//                                        error = result.message
//                                    )
//                                }
                }
            }
//                    }
//                }
//            }
        }
    }
}