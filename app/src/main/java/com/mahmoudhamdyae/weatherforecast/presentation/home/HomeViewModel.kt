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
import kotlinx.coroutines.delay
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

    private var _weather = MutableLiveData<WeatherInfo?>(null)
    val weather: LiveData<WeatherInfo?>
        get() = _weather

    private var _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var _error = MutableLiveData<String?>(null)
    val error: LiveData<String?>
        get() = _error

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
            delay(3000)
            when (val result = repository.getWeather(lat, lon)) {
                is Resource.Success -> {
                    _isLoading.postValue(false)
                    _error.postValue(result.message)
                    _weather.postValue(result.data)
                }
                is Resource.Error -> {
                    _isLoading.postValue(false)
                    _error.postValue(result.message)
                    _weather.postValue(null)
                }
            }
        }
    }
}