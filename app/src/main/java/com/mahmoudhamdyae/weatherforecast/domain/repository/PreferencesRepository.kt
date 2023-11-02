package com.mahmoudhamdyae.weatherforecast.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    val isFirstTime: Flow<Boolean>
    suspend fun setFirstTimePreference()
}