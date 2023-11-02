package com.mahmoudhamdyae.weatherforecast.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.mahmoudhamdyae.weatherforecast.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): PreferencesRepository {

    private companion object {
        val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")
    }

    override val isFirstTime: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            if (preferences[IS_FIRST_TIME] == null || preferences[IS_FIRST_TIME] == true) {
                setFirstTimePreference()
                true
            } else {
                false
            }
        }

    override suspend fun setFirstTimePreference() {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME] = false
        }
    }
}