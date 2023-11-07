package com.mahmoudhamdyae.weatherforecast.presentation.home

import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherInfo

data class HomeUiState(
    val isFirstTime: Boolean = false,
    val weather: WeatherInfo? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
