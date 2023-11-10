package com.mahmoudhamdyae.weatherforecast.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.domain.repository.PreferencesRepository
import com.mahmoudhamdyae.weatherforecast.domain.repository.Repository
import com.mahmoudhamdyae.weatherforecast.domain.util.ApiState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val preferencesRepository: PreferencesRepository,
    private val repository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(preferencesRepository, repository) as T
    }
}

class HomeViewModel (
    preferencesRepository: PreferencesRepository,
    private val repository: Repository
): ViewModel() {

    private var _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.isFirstTime.collect {
                if (it) {
                    // First Time
                    // Navigate to OnBoarding Screen
                    _uiState.value = _uiState.value.copy(isFirstTime = true)
                    this.cancel()
                } else {
                    this.cancel()
                }
            }
        }
    }

    fun getWeather(lat: Double, lon: Double, context: Context) {

        var windSpeed = ""
        var temp = ""
        var lang = ""

        val preferences = PreferenceManager.getDefaultSharedPreferences(context).all

        preferences.forEach {
            when (it.key) {
                "wind_speed" -> {
                    windSpeed = it.value.toString()
                    windSpeed = if (windSpeed == context.getString(R.string.pref_wind_speed_meter)) {
                        "metric"
                    } else {
                        "imperial"
                    }
                }
                "temp" -> {
                    temp = it.value.toString()
                    temp = when (temp) {
                        context.getString(R.string.pref_temp_celsius) -> {
                            "metric"
                        }
                        context.getString(R.string.pref_temp_kelvin) -> {
                            "standard"
                        }
                        else -> { // Fahrenheit
                            "imperial"
                        }
                    }
                }
                "language" -> {
                    lang = it.value.toString()
                    lang = if (lang == context.getString(R.string.pref_language_english)) {
                        "en"
                    } else { // Arabic
                        "ar"
                    }
                }
            }
        }



        viewModelScope.launch {
//            preferences.readLatitude.collect { latitude ->
//                preferences.readLongitude.collect { longitude ->
//                    if (latitude != null && longitude != null) {
//                        _uiState.update {
//                            WeatherUiState(isLoading = true, error = null)
//                        }
//                        when (val result = repository.getWeather(latitude, longitude)) {
            when (
                val result = repository.getWeather(
                    lat = lat,
                    lon = lon,
                    windSpeed = windSpeed,
                    language = lang
                )
            ) {
                is ApiState.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message,
                        weather = result.data
                    )
                }
                is ApiState.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Please, Check your network",
                        weather = null
                    )
                }
            }
        }
    }
}